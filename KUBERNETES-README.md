

# Microservices Deployment Guide for Kubernetes

## Overview

This guide details the deployment of our microservices (Order-Service, Inventory-Service, and Billing-Service) on Kubernetes using Minikube. The deployment includes:

- Three Spring Boot microservices
- MySQL database
- Apache Kafka
- Kubernetes Deployments, Services and ConfigMaps

![kube1.png](screenshots%2Fkubernetes%2Fkube1.png)

## Prerequisites

1. **Minikube:** installed and running  

2. **kubectl** CLI tool installed (installed with minikube installation)  
    - Verify with kubectl version
  ![kube3.png](screenshots%2Fkubernetes%2Fkube3.png)
3. **Docker** installed (for minikube)

## Environment Setup

### 1. Start Minikube

   - Start minikube with minikube start and verify the status.
     

```bash
minikube start
```
```bash
minikube status
```
![kube4.png](screenshots%2Fkubernetes%2Fkube4.png)


### 2. Enable Minikube Docker Environment

```bash
eval $(minikube docker-env)
```

This command configures your terminal to use Minikube's Docker daemon.

![kube5.png](screenshots%2Fkubernetes%2Fkube5.png)

## Building Docker Images

Build the Docker images for each service within Minikube's environment:

- Navigate to each service `order-service`,`inventory-service` and `billing-service` where DockerFile is located.  


- Build docker images for all services one by one. 

<i>Docker image for zookeeper, kafka and mysql is pulled by kubectl through deployment.</i>
```bash
# Build Order Service
cd order-service
docker build -t order-service:latest .

# Build Inventory Service
cd ../inventory-service
docker build -t inventory-service:latest .

# Build Billing Service
cd ../billing-service
docker build -t billing-service:latest .
```

![kube6.png](screenshots%2Fkubernetes%2Fkube6.png)
<div style="text-align: center; margin-top: -20px;">Docker Build : Order Service</div>

![kube7.png](screenshots%2Fkubernetes%2Fkube7.png)
<div style="text-align: center; margin-top: -20px;">Docker Build : Inventory Service</div>

![kube8.png](screenshots%2Fkubernetes%2Fkube8.png)
<div style="text-align: center; margin-top: -20px;">Docker Build : Billing Service</div>

- Verify docker images

```bash
docker images
```
![kube9.png](screenshots%2Fkubernetes%2Fkube9.png)

## Deploying Infrastructure Components
   
   Instead of deploying all services separately, you can directly apply `apply-all-kube-config.yaml`. This file contains all the kubernetes configuration for deployment,service,configmap,resource quota.  
<br>
Navigate to infrastructure directory and run the following command
```bash
kubectl apply -f apply-all-kube-config.yaml
```
![kube10.png](screenshots%2Fkubernetes%2Fkube10.png)

kubectl pull the images for mysql, zookeeper, kafka.
On running the above command, kubectl creates the containers for all services, mysql, zookeeper, kafka.

```bash
docker images
```
```bash
docker ps
```
![kube11.png](screenshots%2Fkubernetes%2Fkube11.png)


Verify the deployment,pods,service
```bash
kubectl get all
```
![kube12.png](screenshots%2Fkubernetes%2Fkube12.png)


## Monitoring

Monitor your deployment using:

```bash
# Get pod status
kubectl get pods 

# Get pod logs
kubectl logs -f <pod-name>

# Get resource usage
kubectl top pods
```

<screenshot: Show monitoring dashboard or CLI output>

## Cleanup

To remove all deployed resources:

```bash
# Delete all deployments from default namespace
kubectl delete all --all -n default

# Stop Minikube
minikube stop
```

## Additional Notes

- All services are configured to use internal Kubernetes DNS for service discovery
- Persistent volumes are used for MySQL data storage
- Services are exposed through Kubernetes Services
- ConfigMaps are used for environment-specific configurations
- Secrets are used for sensitive data like passwords

For any issues or improvements, please create an issue in the repository.
