@echo off

REM Встановлення значень за замовчуванням, якщо не надано
if "%VERSION%"=="" set "VERSION=0.3.0-SNAPSHOT"
if "%GITHUB_OWNER%"=="" set GITHUB_OWNER=chdbc-samples
if "%RESOURCE_GROUP%"=="" set "RESOURCE_GROUP=college-schedule-rg"

REM Перевірка наявності Azure CLI
where az >nul 2>&1

REM Скрипт для розгортання додатка розкладу коледжу в Azure Kubernetes Service (AKS)
REM
REM Перед запуском:
REM 1. Встановіть Azure CLI: https://aka.ms/azure-cli-download
REM 2. Увійдіть в Azure: az login
REM 3. Переконайтеся, що ресурсна група створена та налаштована для отримання образів з GHCR.
REM    (одноразове налаштування для ресурсної групи):
REM
REM    az provider register --namespace Microsoft.Web --wait
REM
REM    az group create `
REM      --name "college-schedule-rg" `
REM      --location "westeurope"

REM az provider register --namespace microsoft.insights --wait

REM Створення AKS кластера...

az aks create `
  --resource-group college-schedule-rg `
  --name college-schedule-aks `
  --node-count 2 `
  --node-vm-size Standard_B2s `
  --location westus2 `
  --generate-ssh-keys `
  --enable-cluster-autoscaler `
  --min-count 1 `
  --max-count 3

REM Отримання облікових даних AKS...
az aks get-credentials `
    --resource-group college-schedule-rg `
    --name college-schedule-aks

REM Отримання subscription ID
$subscriptionId = az account show --query id -o tsv

REM Створення Service Principal
az ad sp create-for-rbac `
  --name "github-actions-aks" `
  --role "Azure Kubernetes Service Cluster User Role" `
  --scopes "/subscriptions/$subscriptionId/resourceGroups/college-schedule-rg/providers/Microsoft.ContainerService/managedClusters/college-schedule-aks" `
  --sdk-auth

REM Create a secret for GitHub Container Registry
REM Replace YOUR_GITHUB_USERNAME and YOUR_GITHUB_PAT with your actual GitHub username and Personal Access Token 
kubectl create secret docker-registry ghcr-secret `
  --docker-server=ghcr.io `
  --docker-username=YOUR_GITHUB_USERNAME `
  --docker-password=YOUR_GITHUB_PAT_WITH_READ_PACKAGES_SCOPE

REM AKS cluster created successfully!
kubectl get nodes

REM Застосовуємо конфігурацію PostgreSQL в кластері
kubectl apply -f deploy/k8s-manifests/postgres-deployment.yml

REM Чекаємо поки pod з PostgreSQL стане готовим до роботи (таймаут 5 хвилин)
kubectl wait --for=condition=ready pod -l app=postgres --timeout=300s

REM Видаляємо попереднє розгортання додатку, якщо воно існує
kubectl delete deployment college-schedule-app --ignore-not-found=true

REM Чекаємо поки старі поди додатку повністю видаляться (таймаут 2 хвилини)
kubectl wait --for=delete pod -l app=college-schedule-app --timeout=120s

REM Розгортаємо новий екземпляр додатку
kubectl apply -f deploy/k8s-manifests/app-deployment.yml

REM Чекаємо поки новий pod додатку стане готовим до роботи (таймаут 10 хвилин)
kubectl wait --for=condition=ready pod -l app=college-schedule-app --timeout=600s

REM Отримуємо зовнішню IP-адресу сервісу для доступу до додатку
kubectl get service college-schedule-app --output jsonpath='{.status.loadBalancer.ingress[0].ip}'
