<#setting number_format = "computer">

<#macro insertExpando
	_userId
>
	<#local groupExpandoColumnId = dataFactory.groupExpandoColumnId>
	<#local groupExpandoTableId = dataFactory.groupExpandoTableId>
	<#local groupId = dataFactory.getGroupId(_userId)>
	<#local rowId = dataFactory.getCounterNext()>
	<#local userExpandoRowModel = dataFactory.newExpandoRowModel(rowId, groupExpandoTableId, groupId)>

	insert into ExpandoRow values (${userExpandoRowModel.rowId}, ${userExpandoRowModel.companyId}, '${dataFactory.getDateString(userExpandoRowModel.modifiedDate)}', ${userExpandoRowModel.tableId}, ${userExpandoRowModel.classPK});

	<#local valueId = dataFactory.getCounterNext()>
	<#local userExpandoValueModel = dataFactory.newExpandoValueModel(valueId, groupExpandoTableId, groupExpandoColumnId, rowId, dataFactory.getGroupClassNameId(), groupId, "true")>

	insert into ExpandoValue values (${userExpandoValueModel.valueId}, ${userExpandoValueModel.companyId}, ${userExpandoValueModel.tableId}, ${userExpandoValueModel.columnId}, ${userExpandoValueModel.rowId}, ${userExpandoValueModel.classNameId}, ${userExpandoValueModel.classPK}, '${userExpandoValueModel.data}');
</#macro>

<#macro insertResourcePermissions
	_entry
>
	<#local resourcePermissionModels = dataFactory.newResourcePermissionModels(_entry)>

	<#list resourcePermissionModels as resourcePermissionModel>
		insert into ResourcePermission values (${resourcePermissionModel.mvccVersion}, ${resourcePermissionModel.resourcePermissionId}, ${resourcePermissionModel.companyId}, '${resourcePermissionModel.name}', ${resourcePermissionModel.scope}, '${resourcePermissionModel.primKey}', ${resourcePermissionModel.roleId}, ${resourcePermissionModel.ownerId}, ${resourcePermissionModel.actionIds});
	</#list>
</#macro>

<#macro insertRole
	_userId
>
	insert into Users_Roles values (${soUserRoleModel.roleId}, ${_userId});
</#macro>

<#macro insertUserLayouts
	_userId
>
	<#local groupId = dataFactory.getGroupId(_userId)>

	<#list dataFactory.userSourcePrototypeLayoutModels as userSourcePrototypeLayoutModel>
		<#local userLayoutModel = dataFactory.newUserLayoutModel(groupId, userSourcePrototypeLayoutModel)>

		insert into Layout values (${userLayoutModel.mvccVersion}, '${userLayoutModel.uuid}', ${userLayoutModel.plid}, ${userLayoutModel.groupId}, ${userLayoutModel.companyId}, ${userLayoutModel.userId}, '${userLayoutModel.userName}', '${dataFactory.getDateString(userLayoutModel.createDate)}', '${dataFactory.getDateString(userLayoutModel.modifiedDate)}', ${userLayoutModel.privateLayout?string}, ${userLayoutModel.layoutId}, ${userLayoutModel.parentLayoutId}, '${userLayoutModel.name}', '${userLayoutModel.title}', '${userLayoutModel.description}', '${userLayoutModel.keywords}', '${userLayoutModel.robots}', '${userLayoutModel.type}', '${userLayoutModel.typeSettings}', ${userLayoutModel.hidden?string}, '${userLayoutModel.friendlyURL}', ${userLayoutModel.iconImageId}, '${userLayoutModel.themeId}', '${userLayoutModel.colorSchemeId}', '${userLayoutModel.wapThemeId}', '${userLayoutModel.wapColorSchemeId}', '${userLayoutModel.css}', ${userLayoutModel.priority}, '${userLayoutModel.layoutPrototypeUuid}', ${userLayoutModel.layoutPrototypeLinkEnabled?string}, '${userLayoutModel.sourcePrototypeLayoutUuid}');

		<#local userLayoutFriendlyURLModel = dataFactory.newLayoutFriendlyURLModel(userLayoutModel)>

		insert into LayoutFriendlyURL values (${userLayoutFriendlyURLModel.mvccVersion}, '${userLayoutFriendlyURLModel.uuid}', ${userLayoutFriendlyURLModel.layoutFriendlyURLId}, ${userLayoutFriendlyURLModel.groupId}, ${userLayoutFriendlyURLModel.companyId}, ${userLayoutFriendlyURLModel.userId}, '${userLayoutFriendlyURLModel.userName}', '${dataFactory.getDateString(userLayoutFriendlyURLModel.createDate)}', '${dataFactory.getDateString(userLayoutFriendlyURLModel.modifiedDate)}', ${userLayoutFriendlyURLModel.plid}, ${userLayoutFriendlyURLModel.privateLayout?string}, '${userLayoutFriendlyURLModel.friendlyURL}', '${userLayoutFriendlyURLModel.languageId}');

		<@insertResourcePermissions
			_entry = userLayoutModel
		/>
	</#list>
</#macro>

<#macro updateGroup
	_userId
>
	update Group_ set typeSettings='${dataFactory.groupTypeSettings}' where groupId=${dataFactory.getGroupId(_userId)};
</#macro>

<#macro updateUserLayoutSet
	_userId
>
	<#local layoutSetPrototypeUserPrivateModel = dataFactory.layoutSetPrototypeUserPrivateModel>

	update LayoutSet set themeId='${dataFactory.themeId}', colorSchemeId='${dataFactory.colorSchemeId}', pageCount=7, settings_='${dataFactory.getSOTypeSettings()}', layoutSetPrototypeUuid='${layoutSetPrototypeUserPrivateModel.uuid}', layoutSetPrototypeLinkEnabled=1 where groupId=${dataFactory.getGroupId(_userId)} and privateLayout=1;

	<#local layoutSetPrototypeUserPublicModel = dataFactory.layoutSetPrototypeUserPublicModel>

	update LayoutSet set themeId='${dataFactory.themeId}', colorSchemeId='${dataFactory.colorSchemeId}', pageCount=4, settings_='${dataFactory.getSOTypeSettings()}', layoutSetPrototypeUuid='${layoutSetPrototypeUserPrivateModel.uuid}', layoutSetPrototypeLinkEnabled=1 where groupId=${dataFactory.getGroupId(_userId)} and privateLayout=0;
</#macro>