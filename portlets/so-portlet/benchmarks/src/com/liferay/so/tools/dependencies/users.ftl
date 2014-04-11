<#assign SORoleId = soUserRoleModel.roleId >

<#list dataFactory.getUserIds() as userId>
	<@insertRole
		_roleId = SORoleId
		_userId = userId
	/>

	<@insertExpando
		_userId = userId
	/>

	<@updateGroup
		_userId = userId
	/>

	<@updateUserLayoutSet
		_userId = userId
	/>
</#list>