<#setting number_format = "computer">

<#-- setupRole -->

<#assign soUserRoleModel = dataFactory.getSOUserRoleModel()>

insert into Role_ values (${soUserRoleModel.mvccVersion}, '${soUserRoleModel.uuid}', ${soUserRoleModel.roleId}, ${soUserRoleModel.companyId}, ${soUserRoleModel.userId}, '${soUserRoleModel.userName}', '${dataFactory.getDateString(soUserRoleModel.createDate)}', '${dataFactory.getDateString(soUserRoleModel.modifiedDate)}', ${soUserRoleModel.classNameId}, ${soUserRoleModel.classPK}, '${soUserRoleModel.name}', '${soUserRoleModel.title}', '${soUserRoleModel.description}', ${soUserRoleModel.type}, '${soUserRoleModel.subtype}');

<#-- setupExpando -->

<#list dataFactory.expandoColumnModels as expandoColumnModel>
	insert into ExpandoColumn values (${expandoColumnModel.columnId}, ${expandoColumnModel.companyId}, ${expandoColumnModel.tableId}, '${expandoColumnModel.name}', ${expandoColumnModel.type}, '${expandoColumnModel.defaultData}', '${expandoColumnModel.typeSettings}');
</#list>

<#list dataFactory.expandoTableModels as expandoTableModels>
    insert into ExpandoTable values (${expandoTableModels.tableId}, ${expandoTableModels.companyId}, ${expandoTableModels.classNameId}, '${expandoTableModels.name}');
</#list>

<#-- initLayoutSetPrototype -->
	<#-- setupLayoutSetPrototypeSite(companyId); -->

	<#-- setupLayoutSetPrototypeUserPrivate(companyId); -->

	<#-- setupLayoutSetPrototypeUserPublic(companyId); -->

<#-- setInitialized -->

<#assign globalGroupModel = dataFactory.getGlobalGroupModel()>

update Group_ set typeSettings = 'social-office-initialized=true' where classPK = ${globalGroupModel.companyId};
