

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

# Step 1:
<h2>Deploy database</h2>


To deploy MySQL with the provided YAML files, the resources need to be created in a specific order to ensure all dependencies are resolved properly. Here's the recommended order:

1. Create the Secret
   File: `mysql-secret.yaml`  
   The MySQL deployment requires the root password from the secret. This must exist before the deployment starts.
    ```bash
    kubectl apply -f mysql-secret.yaml
    ```
   
2. Create the ConfigMap
   File: `mysql-configmap.yaml`
    The ConfigMap contains the initialization script for the database. This must be mounted as a volume when the MySQL container starts.
   ```bash
   kubectl apply -f mysql-configmap.yaml
    ```
   
3. Create the PersistentVolumeClaim (PVC)
   File: `mysql-pvc.yaml`
    The PVC provides persistent storage for MySQL's data. The deployment will mount this PVC, so it must be created first.
   ```bash
   kubectl apply -f mysql-pvc.yaml
    ```

4. Deploy MySQL Service
   File: `mysql-service.yaml`
   The service allows other pods in the Kubernetes cluster to communicate with the MySQL database by abstracting the pod IPs.
   Exposes MySQL on port 3306 (the default port for MySQL).
   ```bash
    kubectl apply -f mysql-service.yaml
   ```
   
5. Deploy MySQL
   File: `mysql-deployment.yaml`
    The deployment will reference the Secret, ConfigMap, and PVC. Ensure these resources are created beforehand.
   ```bash
    kubectl apply -f mysql-deployment.yaml
    ```

Run the commands in the following order:

```bash

kubectl apply -f mysql-secret.yaml
kubectl apply -f mysql-configmap.yaml
kubectl apply -f mysql-pvc.yaml
kubectl apply -f mysql-service.yaml
kubectl apply -f mysql-deployment.yaml

```

![Screenshot 2025-01-05 at 7.56.05 PM.png](screenshots%2Fkubernetes%2FScreenshot%202025-01-05%20at%207.56.05%E2%80%AFPM.png)

After applying these resources, you can verify the deployment:
```bash
# Check the pods
kubectl get pods

# Check the Persistent Volume and Persistent Volume Claim
kubectl get pv
kubectl get pvc

# Check the ConfigMap and Secret
kubectl get configmap
kubectl get secret

kubectl get deployments
kubectl get service
```

![Screenshot 2025-01-05 at 7.44.23 PM.png](screenshots%2Fkubernetes%2FScreenshot%202025-01-05%20at%207.44.23%E2%80%AFPM.png)

# Step 2:
<h2>Deploy all other services</h2>

Navigate to infrastructure directory and run the following command. The following commands create necessary config, deployment,services for zookeeper, kafka, order-service, inventory-service and billing-service.
```bash
kubectl apply -f apply-all-kube-config.yaml
```
![kube10.png](screenshots%2Fkubernetes%2Fkube10.png)

- **_kubectl pull the images for mysql, zookeeper, kafka._**
<br>
<br>
    Verify with following

    ```bash
    docker images
    ```
    ```bash
    docker ps
    ```
    ![kube11.png](screenshots%2Fkubernetes%2Fkube11.png)

    Verify the deployment,pods,service with following command
    
    ```bash
    kubectl get all
    ```
![kube12.png](screenshots%2Fkubernetes%2Fkube12.png)



## Once all pods are up and running, verify the following
    
   - Kafka topics is created.
   - All tables are created in mysql
   - Service is able to produce message to topic.
   - Service is able to consume the message.
   - Verify the tables are updated

### 1.Kafka topics is created.

Execute the kafka pod with following command

```bash
kubectl exec -it <your-pod-name> -- /bin/bash
```

```bash
kubectl exec -it kafka-8cf75665c-57wt2 -- /bin/bash
```

![Screenshot 2025-01-05 at 8.10.20 PM.png](screenshots%2Fkubernetes%2FScreenshot%202025-01-05%20at%208.10.20%E2%80%AFPM.png)

Navigate to kafka directory.

```bash
cd /opt/kafka
```

Get the list of kafka-topics with following command:

```
./bin/kafka-topics.sh --bootstrap-server localhost:9092 --list
```
![Screenshot 2025-01-05 at 8.13.41 PM.png](screenshots%2Fkubernetes%2FScreenshot%202025-01-05%20at%208.13.41%E2%80%AFPM.png)

Get the list of consumer group id.

```bash
  ./bin/kafka-consumer-groups.sh --bootstrap-server localhost:9092 --list
```

![Screenshot 2025-01-05 at 8.34.31 PM.png](screenshots%2Fkubernetes%2FScreenshot%202025-01-05%20at%208.34.31%E2%80%AFPM.png)

`billing-group-id` and `inventory-group-id` are created and ready to consume the messages

### 2.All tables are created in mysql

Execute the mysql pod with following command

```bash
kubectl exec -it <your-pod-name> -- /bin/bash
```

```bash
kubectl exec -it mysql-55d56f565d-fckkb -- /bin/bash
```
Login to mysql server

```bash
mysql -u root -p
```

Enter password on prompt.

Verify the database with following

```bash
show databases;

use product_database;

show tables;
```

![Screenshot 2025-01-05 at 8.41.03 PM.png](screenshots%2Fkubernetes%2FScreenshot%202025-01-05%20at%208.41.03%E2%80%AFPM.png)

_All tables are created._

### 3.Service is able to produce message to topic

The `order-service` app is used for producing message to the topic.
It provides POST endpoint to produce message.

The endpoint can be executed within and outside the cluster.
- Within cluster  

    To execute the endpoint with curl, execute order-service pod and use curl to hit the endpoint.

    - Use following command to execute order-service pod
    
    ```bash
    kubectl exec -it <your-pod-name> -- /bin/bash
    ```
   ```bash
    kubectl exec -it order-service-deployment-7797579499-f4hxv -- /bin/bash
    ```
   - Once executed inside pod, use curl command to hit endpoint 

    ```bash
    curl -X POST http://localhost:8080/orders/place-order -H "Content-Type: application/json" -d '{"productName":"Samsung Galaxy S23","quantity":1,"price":999.99}'
    ```
    ![Screenshot 2025-01-05 at 10.02.58 PM.png](screenshots%2Fkubernetes%2FScreenshot%202025-01-05%20at%2010.02.58%E2%80%AFPM.png)
    
    - Verify the application logs
    ```bash
    kubectl logs -f <pod_name>
    ```
  ```bash
  kubectl logs -f order-service-deployment-7797579499-f4hxv
  kubectl logs -f billing-service-deployment-5bcd779767-pbskn
  kubectl logs -f inventory-service-deployment-75bbbfcb9f-f8rfr
  ```            
  ![Screenshot 2025-01-05 at 9.44.12 PM.png](screenshots%2Fkubernetes%2FScreenshot%202025-01-05%20at%209.44.12%E2%80%AFPM.png)


### 4.Verify message is consumed

- Execute the kafka pod with following command

```bash
kubectl exec -it <your-pod-name> -- /bin/bash
```

```bash
kubectl exec -it kafka-8cf75665c-57wt2 -- /bin/bash
```

- Check the messages consumed in order-topic
    - Navigate to kafka directory
    ```bash
  cd /opt/kafka  
  ```
    - Enter the following command
  ```bash
    ./bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic order-topic --from-beginning
    ```
  ![Screenshot 2025-01-05 at 9.55.11 PM.png](screenshots%2Fkubernetes%2FScreenshot%202025-01-05%20at%209.55.11%E2%80%AFPM.png)

### 5.Verify the tables are updated

Execute the mysql pod with following command

```bash
kubectl exec -it <your-pod-name> -- /bin/bash
```

```bash
kubectl exec -it mysql-55d56f565d-fckkb -- /bin/bash
```
Login to mysql server

```bash
mysql -u root -p
```

Enter password on prompt.

Verify the database with following.

```bash
show databases;

use product_database;

select * from inventory;

select * from orders;

select * from billing;

```

![Screenshot 2025-01-05 at 10.01.35 PM.png](screenshots%2Fkubernetes%2FScreenshot%202025-01-05%20at%2010.01.35%E2%80%AFPM.png)

<i>All tables are updated. Services are able to communicate with each other with kafka messaging.</i>

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
