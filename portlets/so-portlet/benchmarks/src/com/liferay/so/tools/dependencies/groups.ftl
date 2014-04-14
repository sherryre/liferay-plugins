<#list dataFactory.groupModels as groupModel>
	insert into Group_ values (${groupModel.mvccVersion}, '${groupModel.uuid}', ${groupModel.groupId}, ${groupModel.companyId}, ${groupModel.creatorUserId}, ${groupModel.classNameId}, ${groupModel.classPK}, ${groupModel.parentGroupId}, ${groupModel.liveGroupId}, '${groupModel.treePath}', '${groupModel.name}', '${groupModel.description}', ${groupModel.type}, '${groupModel.typeSettings}', ${groupModel.manualMembership?string}, ${groupModel.membershipRestriction}, '${groupModel.friendlyURL}', ${groupModel.site?string}, ${groupModel.remoteStagingGroupCount}, ${groupModel.active?string});
</#list>

update Group_ set typeSettings = 'social-office-initialized=true social-office-users-configured=true' where classPK = ${dataFactory.companyId};
