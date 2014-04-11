<#setting number_format = "computer">

<#macro insertRole
	_userId
	_roleId
>
	insert into users_roles values(${_userId}, ${_roleId});
</#macro>

<#macro insertExpando
	_userId
>
	<#local rowId = dataFactory.getCounterNext()>
	<#local userExpandoRowModel = dataFactory.newExpandoRowModel(rowId, dataFactory.groupExpandoTableId, dataFactory.getGroupId(_userId))>

	insert into ExpandoRow values (${userExpandoRowModel.rowId}, ${userExpandoRowModel.companyId}, '${dataFactory.getDateString(userExpandoRowModel.modifiedDate)}', ${userExpandoRowModel.tableId}, ${userExpandoRowModel.classPK});

	<#local userExpandoValueModel = dataFactory.newExpandoValueModel(dataFactory.getCounterNext(), dataFactory.groupExpandoTableId, dataFactory.groupExpandoColumnId, rowId, dataFactory.getGroupId(_userId), dataFactory.getGroupClassNameId(), "true")>

	insert into ExpandoValue values (${userExpandoValueModel.valueId}, ${userExpandoValueModel.companyId}, ${userExpandoValueModel.tableId}, ${userExpandoValueModel.columnId}, ${userExpandoValueModel.rowId}, ${userExpandoValueModel.classNameId}, ${userExpandoValueModel.classPK}, '${userExpandoValueModel.data}');
</#macro>

<#macro updateGroup
	_userId
>
	<#local treePath = dataFactory.getFriendlyURL(_userId) + "/">

	update Group_ set mvccVersion=0, uuid_='${dataFactory.getUUID()}',  companyId=${dataFactory.companyId}, creatorUserId=${_userId}, classNameId=${dataFactory.userClassNameId}, classPK=${_userId}, parentGroupId=0, liveGroupId=0, treePath='${treePath}', name='${_userId}', description='', type_=0, typeSettings='${dataFactory.groupTypeSettings}', manualMembership=1, membershipRestriction=0, friendlyURL='${dataFactory.getFriendlyURL(_userId)}', site=0, remoteStagingGroupCount=0, active_=1 where groupId=${dataFactory.getGroupId(_userId)};
</#macro>

<#macro updateUserLayoutSet
	_userId
>
	<#local layoutSetPrototypeUserPrivate = dataFactory.layoutSetPrototypeUserPrivateModel>

	update LayoutSet set mvccVersion = 0, layoutSetId=${dataFactory.getCounterNext()}, groupId=${dataFactory.getGroupId(_userId)}, companyId=${dataFactory.companyId}, createDate='${dataFactory.getDate()}', modifiedDate='${dataFactory.getDate()}', privateLayout=1, logoId=0, themeId='${dataFactory.themeId}', colorSchemeId='${dataFactory.colorSchemeId}', wapThemeId='mobile', wapColorSchemeId='01', css='', pageCount=7, settings_='${dataFactory.getSOTypeSettings()}', layoutSetPrototypeUuid='${layoutSetPrototypeUserPrivate.uuid}', layoutSetPrototypeLinkEnabled=1 where groupId=${dataFactory.getGroupId(_userId)} and privateLayout=1;

	<#local layoutSetPrototypeUserPublic = dataFactory.layoutSetPrototypeUserPublicModel>

	update LayoutSet set mvccVersion = 0, layoutSetId=${dataFactory.getCounterNext()}, groupId=${dataFactory.getGroupId(_userId)}, companyId=${dataFactory.companyId}, createDate='${dataFactory.getDate()}', modifiedDate='${dataFactory.getDate()}', privateLayout=1, logoId=0, themeId='${dataFactory.themeId}', colorSchemeId='${dataFactory.colorSchemeId}', wapThemeId='mobile', wapColorSchemeId='01', css='', pageCount=7, settings_='${dataFactory.getSOTypeSettings()}', layoutSetPrototypeUuid='${layoutSetPrototypeUserPublic.uuid}', layoutSetPrototypeLinkEnabled=1 where groupId=${dataFactory.getGroupId(_userId)} and privateLayout=0;
</#macro>