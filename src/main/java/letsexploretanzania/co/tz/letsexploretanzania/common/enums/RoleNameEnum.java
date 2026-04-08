package letsexploretanzania.co.tz.letsexploretanzania.common.enums;

public enum RoleNameEnum
{
    INVALID,
    TOURIST,
    GUIDE,
    ADMIN,
    OPERATOR;


    public static RoleNameEnum getFromName(String roleName)
    {
        RoleNameEnum result = RoleNameEnum.INVALID;
        for (RoleNameEnum role : values())
        {
            if (role.name().equalsIgnoreCase(roleName))
                result = role;
        }
        return result;
    }
}


