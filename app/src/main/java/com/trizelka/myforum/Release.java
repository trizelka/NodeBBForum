package com.trizelka.myforum;

/**
 * Release-specific variables.
 *
 * @author Trisia Juniarto
 *
 */
public interface Release {
  public static final boolean SSL                     = false;
  public static final String MASTER_SERVER_HOST       = "128.199.73.20";
  public static final int MASTER_SERVER_PORT          = 4567;
  public static final String UrlgetConfig                 = "http://"+ MASTER_SERVER_HOST +":"+ MASTER_SERVER_PORT + "/api/config/"; //GET
  public static final String UrlLogin                 = "http://"+ MASTER_SERVER_HOST +":"+ MASTER_SERVER_PORT + "/login"; //POST
  public static final String UrlSignUp                 = "http://"+ MASTER_SERVER_HOST +":"+ MASTER_SERVER_PORT + "/signup/"; //POST
  public static final String UrlGetPublic                 = "http://"+ MASTER_SERVER_HOST +":"+ MASTER_SERVER_PORT + "/api/"; //GET
  public static final String UrlGetRegister              = "http://"+ MASTER_SERVER_HOST +":"+ MASTER_SERVER_PORT + "/api/register/"; //GET
  public static final String UrlGetUsers            = "http://"+ MASTER_SERVER_HOST +":"+ MASTER_SERVER_PORT + "/api/users/"; //GET
  public static final String UrlGetProfile            = "http://"+ MASTER_SERVER_HOST +":"+ MASTER_SERVER_PORT + "/api/user/"; //GET
  public static final String UrlLogout            = "http://"+ MASTER_SERVER_HOST +":"+ MASTER_SERVER_PORT + "/logout"; //POST
}
