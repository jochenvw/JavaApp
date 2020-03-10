New-AzResourceGroup `
    -Name ms-csu-nl-jvw-javademoapp-test `
    -Location westeurope


New-AzResourceGroupDeployment `
    -ResourceGroupName ms-csu-nl-jvw-javademoapp-test `
    -TemplateFile .\application-resources.json `
    -TemplateParameterFile .\parameters.test.json `
    -Verbose

New-AzResourceGroupDeployment `
    -ResourceGroupName ms-csu-nl-jvw-javademoapp-test `
    -TemplateFile .\dashboard.json `
    -TemplateParameterFile .\parameters.test.json `
    -Verbose