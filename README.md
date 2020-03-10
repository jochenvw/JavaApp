# Sping boot on Azure WebApps demo application

Attempt to automat as much a possible a modern Java App that runs on Azure WebApps, taking into account security and instrumentation practices that I think need to be in place.


## Build status
[![Build Status](https://dev.azure.com/jvwdemo/JavaApp/_apis/build/status/Application%20CI%20Build?branchName=master)](https://dev.azure.com/jvwdemo/JavaApp/_build/latest?definitionId=55&branchName=master)

## Azure services
Public traffic will hit the following services:
1. API Management
2. Application Gateway
3. Web Application

And support services include:

4. Azure KeyVault
5. A Virtual Network - for service endpoints etc.
6. Application Insights



## Build
Build pipeline is included as `.yml` in the `/build` folder:

[/build/build.yml](build/build.yml)

**Please note** The build pipeline assumes to variables to be set in Azure DevOps:
1. `serviceconnection` - Name of your service connection that has rights to deploy inside the Azure Subscription
2. `subsribptionid` - GUID with the ID of the subscription to deploy to

## Deploy
ARM template is included in the `/infra/` folder:

[/infra/application-resources.json](/infra/application-resources.json)

And template deployment can be tested locally using the `_deploy.ps1` file - in the same folder.




## License

```
Copyright 2020

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
```