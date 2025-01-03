# Minikube Installation Guide

This guide provides step-by-step instructions to install Minikube and some commonly used commands for managing your Kubernetes cluster.

## Prerequisites
- A supported operating system (Linux, macOS, or Windows)
- Installed:
    - [Docker](https://docs.docker.com/get-docker/), [VirtualBox](https://www.virtualbox.org/), or another supported VM driver
    - [kubectl](https://kubernetes.io/docs/tasks/tools/)

## Installing Minikube

### On Linux
1. Download the latest Minikube binary:
   ```bash
   curl -LO https://github.com/kubernetes/minikube/releases/latest/download/minikube-linux-amd64/
   ```

2. Install Minikube:
   ```bash
   sudo install minikube-linux-amd64 /usr/local/bin/minikube && rm minikube-linux-amd64
   ```

3. Verify the installation:
   ```bash
   minikube version
   ```

### On macOS
1. Install Minikube using Homebrew:
   ```bash
   brew install minikube
   ```

2. Verify the installation:
   ```bash
   minikube version
   ```

![minikube1.png](screenshots%2Fminikube%2Fminikube1.png)


### On Windows
1. Install Minikube via [choco](https://chocolatey.org/):
   ```powershell
   choco install minikube
   ```

2. Verify the installation:
   ```powershell
   minikube version
   ```

## Starting Minikube
To start a Minikube cluster:

```bash
minikube start
```

![minikube2.png](screenshots%2Fminikube%2Fminikube2.png)

### Verifying Minikube Status
Check the status of your Minikube cluster:

```bash
minikube status
```

![minikube3.png](screenshots%2Fminikube%2Fminikube3.png)

## Useful Minikube Commands

### Stopping Minikube
Stop the Minikube cluster:

```bash
minikube stop
```

### Deleting Minikube Cluster
Delete the Minikube cluster:

```bash
minikube delete
```

### Interacting with Docker Inside Minikube
To interact with the Docker environment used by Minikube:

```bash
eval $(minikube docker-env)
```

### Viewing Minikube Dashboard
Launch the Minikube Kubernetes Dashboard:

```bash
minikube dashboard
```

### SSH Into Minikube Node
SSH into the Minikube virtual machine:

```bash
minikube ssh
```

### Enable Add-Ons
Enable useful Minikube add-ons like metrics-server or ingress:

```bash
minikube addons enable metrics-server
```

### Checking Cluster Logs
View logs for troubleshooting:

```bash
minikube logs
```

### Getting Cluster IP
Retrieve the Minikube cluster IP:

```bash
minikube ip
```

## Cleanup
To clean up all Minikube-related resources, delete the cluster:

```bash
minikube delete
```

---

For more details, visit the [official Minikube documentation](https://minikube.sigs.k8s.io/docs/).
