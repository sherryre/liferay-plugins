<#setting number_format = "computer">

<#macro insertRole
	_userId
	_roleId
>
	insert into users_roles values(${_userId}, ${_roleId});
</#macro>