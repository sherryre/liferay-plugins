<#setting number_format = "computer">

<#macro insertRole
	_userId
>
	insert into Users_Roles values (${_userId}, ${soUserRoleModel.roleId});
</#macro>

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

<#macro updateGroup
	_userId
>
	update Group_ set typeSettings='${dataFactory.groupTypeSettings}' where groupId=${dataFactory.getGroupId(_userId)};
</#macro>

<#macro updateUserLayoutSet
	_userId
>
	<#local layoutSetPrototypeUserPrivateModel = dataFactory.layoutSetPrototypeUserPrivateModel>

	update LayoutSet set  themeId='${dataFactory.themeId}', colorSchemeId='${dataFactory.colorSchemeId}', pageCount = 7, settings_='${dataFactory.getSOTypeSettings()}', layoutSetPrototypeUuid='${layoutSetPrototypeUserPrivateModel.uuid}', layoutSetPrototypeLinkEnabled=1 where groupId=${dataFactory.getGroupId(_userId)} and privateLayout=1;

	<#local layoutSetPrototypeUserPublicModel = dataFactory.layoutSetPrototypeUserPublicModel>

	update LayoutSet set  themeId='${dataFactory.themeId}', colorSchemeId='${dataFactory.colorSchemeId}', pageCount = 4, settings_='${dataFactory.getSOTypeSettings()}', layoutSetPrototypeUuid='${layoutSetPrototypeUserPrivateModel.uuid}', layoutSetPrototypeLinkEnabled=1 where groupId=${dataFactory.getGroupId(_userId)} and privateLayout=0;
</#macro>

<#macro updateUserLayouts
	_userId
>
	<#local groupId = dataFactory.getGroupId(_userId)>

	<#list dataFactory.userLayoutModels as userLayoutModel>
		<#local plid = dataFactory.getCounterNext()>
		<#local typeSettings = dataFactory.getSOTypeSettings() + " " + userLayoutModel.typeSettings>

		insert into layout values (${userLayoutModel.mvccVersion}, '${userLayoutModel.uuid}', ${plid}, ${groupId}, ${userLayoutModel.companyId}, 0, '', '${dataFactory.getDateString(userLayoutModel.createDate)}', '${dataFactory.getDateString(userLayoutModel.modifiedDate)}', ${userLayoutModel.privateLayout?string}, ${dataFactory.getCounterNext()}, ${userLayoutModel.parentLayoutId}, '${userLayoutModel.name}', '', '', '', '', '${userLayoutModel.type}', '${typeSettings}', ${userLayoutModel.hidden?string}, '${userLayoutModel.friendlyURL}', ${userLayoutModel.iconImageId}, '${userLayoutModel.themeId}', '${userLayoutModel.colorSchemeId}', '${userLayoutModel.wapThemeId}', '${userLayoutModel.wapColorSchemeId}', '${userLayoutModel.css}', ${userLayoutModel.priority}, '${userLayoutModel.layoutPrototypeUuid}', ${userLayoutModel.layoutPrototypeLinkEnabled?string}, '${userLayoutModel.uuid}');
		insert into layoutFriendlyURL values (${userLayoutModel.mvccVersion}, '${dataFactory.getUUID()}', ${dataFactory.getCounterNext()},${groupId}, ${dataFactory.companyId}, ${_userId}, '', '${dataFactory.getDate()}', '${dataFactory.getDate()}', ${plid}, '${userLayoutModel.privateLayout?string}', '${userLayoutModel.friendlyURL}', 'en_US');
	</#list>
</#macro>