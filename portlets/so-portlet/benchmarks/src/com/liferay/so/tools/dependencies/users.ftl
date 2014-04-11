<#assign SORoleId = soUserRoleModel.roleId >

<#list dataFactory.getUserIds() as userId>
	<@insertRole
		_roleId = SORoleId
		_userId = userId
	/>
</#list>