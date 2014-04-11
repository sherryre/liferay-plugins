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