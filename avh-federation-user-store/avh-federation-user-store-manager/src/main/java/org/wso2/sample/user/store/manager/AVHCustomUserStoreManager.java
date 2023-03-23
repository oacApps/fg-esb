package org.wso2.sample.user.store.manager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.CarbonConstants;
import org.wso2.carbon.user.api.Properties;
import org.wso2.carbon.user.api.Property;
import org.wso2.carbon.user.core.UserRealm;
import org.wso2.carbon.user.core.UserStoreException;
import org.wso2.carbon.user.core.claim.ClaimManager;
import org.wso2.carbon.user.core.jdbc.JDBCRealmConstants;
import org.wso2.carbon.user.core.jdbc.JDBCUserStoreManager;
import org.wso2.carbon.user.core.profile.ProfileConfigurationManager;
import org.wso2.carbon.user.core.util.DatabaseUtil;
import org.wso2.carbon.utils.Secret;
import org.wso2.carbon.utils.UnsupportedSecretTypeException;

import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class AVHCustomUserStoreManager extends JDBCUserStoreManager {
	private static Log log = LogFactory.getLog(AVHCustomUserStoreManager.class);
	public AVHCustomUserStoreManager() {
	}
	public AVHCustomUserStoreManager(org.wso2.carbon.user.api.RealmConfiguration realmConfig,
										 Map<String, Object> properties,
										 ClaimManager claimManager,
										 ProfileConfigurationManager profileManager,
										 UserRealm realm, Integer tenantId)
			throws UserStoreException {
		super(realmConfig, properties, claimManager, profileManager, realm, tenantId, false);
	}
	@Override
	public boolean doAuthenticate(String userName, Object credential) throws UserStoreException {
		if (CarbonConstants.REGISTRY_ANONNYMOUS_USERNAME.equals(userName)) {
			log.error("Anonymous user trying to login");
			return false;
		}
		PasswordEncription encription = new PasswordEncription();
		Connection dbConnection = null;
		ResultSet rs = null;
		PreparedStatement prepStmt = null;
		String sqlstmt = null;
		String password = String.copyValueOf(((Secret) credential).getChars());
		String storedPassword="";
		boolean isAuthed = false;
		try {
			dbConnection = getDBConnection();
			dbConnection.setAutoCommit(false);
//paring the SELECT_USER_SQL from user_mgt.xml
			sqlstmt = realmConfig.getUserStoreProperty(JDBCRealmConstants.SELECT_USER);
			if (log.isDebugEnabled()) {
				log.debug(sqlstmt);
			}
			prepStmt = dbConnection.prepareStatement(sqlstmt);
			prepStmt.setString(1, userName);
			rs = prepStmt.executeQuery();
			if (rs.next()) {
				storedPassword = rs.getString("password");
//				if ((storedPassword != null) && (storedPassword.trim().equals(encription.encryptedPassword(password)))) {
//					isAuthed = true;
//				}
//				if ((storedPassword != null) && (storedPassword.trim().equals(password))) {
//					isAuthed = true;
//				}
				isAuthed = true;
			}
		} catch (SQLException e) {
			throw new UserStoreException("Authentication Failure. Using sql :" + sqlstmt + " " + password + " " + storedPassword);
		} finally {
			DatabaseUtil.closeAllConnections(dbConnection, rs, prepStmt);
		}
		if (log.isDebugEnabled()) {
			log.debug("User " + userName + " login attempt. Login success :: " + isAuthed);
		}
		return isAuthed;
	}
	@Override
	public Date getPasswordExpirationTime(String userName) throws UserStoreException {
		return null;
	}
	protected boolean isValueExisting(String sqlStmt, Connection dbConnection, Object... params)
			throws UserStoreException {
		PreparedStatement prepStmt = null;
		ResultSet rs = null;
		boolean isExisting = false;
		boolean doClose = false;
		try {
			if (dbConnection == null) {
				dbConnection = getDBConnection();
				doClose = true; //because we created it
			}
			if (DatabaseUtil.getStringValuesFromDatabase(dbConnection, sqlStmt, params).length > 0) {
				isExisting = true;
			}
			return isExisting;
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
			log.error("Using sql : " + sqlStmt);
			throw new UserStoreException(e.getMessage(), e);
		} finally {
			if (doClose) {
				DatabaseUtil.closeAllConnections(dbConnection, rs, prepStmt);
			}
		}
	}

	public String[] getUserListFromProperties(String property, String value, String profileName)
			throws UserStoreException {
		return new String[0];
	}

	/*@Override
    public Map<String, String> doGetUserClaimValues(String userName, String[] claims,
    String domainName) throws UserStoreException {
    return new HashMap<String, String>();
    }*/
/*@Override
public String doGetUserClaimValue(String userName, String claim, String profileName)
throws UserStoreException {
return null;
}*/
//	@Override
//	public boolean isReadOnly() throws UserStoreException {
//		return false;
//	}
	@Override
	public void doAddUser(String userName, Object credential, String[] roleList,
						  Map<String, String> claims, String profileName,
						  boolean requirePasswordChange) throws UserStoreException {
//		PasswordEncription encription = new PasswordEncription();
//		String password = String.copyValueOf(((Secret)credential).getChars());
//		String encryptedpassword = null;
//		try {
//			encryptedpassword = encription.encryptedPassword(password);
//		} catch (NoSuchAlgorithmException e) {
//			e.printStackTrace();
//		}
//		log.info("password entered : " +password);
//		claims.get("http://wso2.org/claims/emails.home");
//		Connection dbConnection = null;
//		ResultSet rs = null;
//		PreparedStatement prepStmt = null;
//		String sqlstmt = "INSERT INTO go121Identity (createdBy, modifiedDate, modifiedBy, userName, password, firstName, lastName, email, mobileNumber) " +
//				"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
//		String storedPassword="";
//		boolean isAuthed = false;
//		try {
//			dbConnection = getDBConnection();
//			dbConnection.setAutoCommit(false);
//			if (log.isDebugEnabled()) {
//				log.debug(sqlstmt);
//			}
//			prepStmt = dbConnection.prepareStatement(sqlstmt);
//			prepStmt.setString(1, "");
//			prepStmt.setString(2, claims.get("http://wso2.org/claims/modified"));
//			prepStmt.setString(3, "");
//			prepStmt.setString(4, userName);
//			prepStmt.setString(5, encryptedpassword);
//			prepStmt.setString(6, "");
//			prepStmt.setString(7, claims.get("http://wso2.org/claims/lastname"));
//			prepStmt.setString(8, claims.get("http://wso2.org/claims/emails.work"));
//			prepStmt.setString(9, "");
//			prepStmt.execute();
//			dbConnection.commit();
//			dbConnection.close();
//			prepStmt.close();
//
//		} catch (SQLException e) {
//			throw new UserStoreException("Authentication Failure. Using sql");
//		} finally {
//			DatabaseUtil.closeAllConnections(dbConnection, prepStmt);
//		}
//		if (log.isDebugEnabled()) {
//			log.debug("User " + userName + " login attempt. Login success :: " + isAuthed);
//		}

	}
	public void doAddRole(String roleName, String[] userList, org.wso2.carbon.user.api.Permission[] permissions)
			throws UserStoreException {
		throw new UserStoreException(
				"User store is operating in read only mode. Cannot write into the user store.");
	}
	@Override
	public void doDeleteRole(String roleName) throws UserStoreException {
		throw new UserStoreException(
				"User store is operating in read only mode. Cannot write into the user store.");
	}
	@Override
	public void doDeleteUser(String userName) throws UserStoreException {
		throw new UserStoreException(
				"User store is operating in read only mode. Cannot write into the user store.");
	}
	@Override
	public boolean isBulkImportSupported() {
		return false;
	}
	@Override
	public void doUpdateRoleName(String roleName, String newRoleName) throws UserStoreException {
		throw new UserStoreException(
				"User store is operating in read only mode. Cannot write into the user store.");
	}
	@Override
	public void doUpdateUserListOfRole(String roleName, String[] deletedUsers, String[] newUsers)
			throws UserStoreException {
		throw new UserStoreException(
				"User store is operating in read only mode. Cannot write into the user store.");
	}
	@Override
	public void doUpdateRoleListOfUser(String userName, String[] deletedRoles, String[] newRoles)
			throws UserStoreException {
		throw new UserStoreException(
				"User store is operating in read only mode. Cannot write into the user store.");
	}
	@Override
	public void doSetUserClaimValue(String userName, String claimURI, String claimValue,
									String profileName) throws UserStoreException {
		throw new UserStoreException(
				"User store is operating in read only mode. Cannot write into the user store.");
	}
	@Override
	public void doSetUserClaimValues(String userName, Map<String, String> claims,
									 String profileName) throws UserStoreException {
		throw new UserStoreException(
				"User store is operating in read only mode. Cannot write into the user store.");
	}
	@Override
	public void doDeleteUserClaimValue(String userName, String claimURI, String profileName)
			throws UserStoreException {
		throw new UserStoreException(
				"User store is operating in read only mode. Cannot write into the user store.");
	}
	@Override
	public void doDeleteUserClaimValues(String userName, String[] claims, String profileName)
			throws UserStoreException {
		throw new UserStoreException(
				"User store is operating in read only mode. Cannot write into the user store.");
	}
	@Override
	public void doUpdateCredential(String userName, Object newCredential, Object oldCredential)
			throws UserStoreException {
		throw new UserStoreException(
				"User store is operating in read only mode. Cannot write into the user store.");
	}
	@Override
	public void doUpdateCredentialByAdmin(String userName, Object newCredential)
			throws UserStoreException {
		throw new UserStoreException(
				"User store is operating in read only mode. Cannot write into the user store.");
	}
	public String[] getExternalRoleListOfUser(String userName) throws UserStoreException {
		return new String[0];
	}
	@Override
	public String[] doGetRoleNames(String filter, int maxItemLimit) throws UserStoreException {
		log.info("doGetRoleNames called.");
		return new String[0];
	}

	@Override
	public String[] doGetExternalRoleListOfUser(String userName, String filter) throws UserStoreException {
		log.info("doGetExternalRoleListOfUser called.");
		return new String[0];
	}

	@Override
	protected String[] doGetSharedRoleListOfUser(String userName, String tenantDomain, String filter) throws UserStoreException {
		log.info("doGetSharedRoleListOfUser called.");
		return new String[0];
	}

	@Override
	public boolean doCheckIsUserInRole(String userName, String roleName) throws UserStoreException {
		log.info("doCheckIsUserInRole called.");
		return true;
	}

	@Override
	protected String[] doGetSharedRoleNames(String tenantDomain, String filter, int maxItemLimit) throws UserStoreException {
		log.info("doGetSharedRoleNames called.");
		return new String[0];
	}

	@Override
	public String[] doGetUserListOfRole(String roleName, String filter) throws UserStoreException {
		log.info("doGetUserListOfRole called.");
		return new String[0];
	}

	@Override
	protected String[] doGetDisplayNamesForInternalRole(String[] userNames) throws UserStoreException {
		log.info("doGetDisplayNamesForInternalRole called.");
		return new String[0];
	}

	@Override
	public String[] getProfileNames(String userName) {
		return new String[0];
	}
	@Override
	public boolean doCheckExistingRole(String roleName) throws UserStoreException {
		log.info("doCheckExistingRole called.");
		return false;
	}

	@Override
	public Map<String, String> getUserPropertyValues(String userName,
													 String[] propertyNames, String profileName)
			throws UserStoreException
	{
		Connection dbConnection = null;
		String sqlStmt = null;
		PreparedStatement prepStmt = null;
		ResultSet rs = null;
		String[] propertyNamesSorted = propertyNames.clone();
		Arrays.sort(propertyNamesSorted);
		Map<String, String> map = new HashMap<String, String>();
		try
		{
			dbConnection = getDBConnection();
			sqlStmt = realmConfig.getUserStoreProperty(
					JDBCRealmConstants.GET_PROPS_FOR_PROFILE);
//		sqlStmt = "DECLARE @colsUnpivot AS NVARCHAR(MAX),@query AS NVARCHAR(MAX) select @colsUnpivot = '[UM_USER_NAME],[UM_USER_UUID]' set @query = 'select UM_ATTR_NAME, UM_ATTR_VALUE from (select * from UM_USER where UM_USER_NAME = ''kirthan'') u2 unpivot(UM_ATTR_VALUE for UM_ATTR_NAME in ('+ @colsunpivot +')) u' exec sp_executesql @query";
			prepStmt = dbConnection.prepareStatement(sqlStmt);
			prepStmt.setString(1, userName);

			rs = prepStmt.executeQuery();
			while (rs.next())
			{
				for (String name : propertyNamesSorted)
				{
					String value = null;
					switch (name)
					{
						case "UM_ID":
							value = rs.getString(1);
							break;
						case "UM_USER_NAME":
							value = rs.getString(2);
							break;
						default:
							break;
					}

					if(value != null) {
						map.put(name, value);
					}

				}
			}

			return map;
		} catch (SQLException e)
		{
			String errorMessage = "Error Occurred while getting property values for user : "
					+ userName + " & profile name : " + profileName;
			if (log.isDebugEnabled())
			{
				log.debug(errorMessage, e);
			}
			throw new UserStoreException(errorMessage, e);
		} finally
		{
			DatabaseUtil.closeAllConnections(dbConnection, rs, prepStmt);
		}
	}

	@Override
	public boolean doCheckExistingUser(String userName) throws UserStoreException {
		String sqlStmt = this.realmConfig.getUserStoreProperty("SelectUserSQL");
		if (sqlStmt == null)
			throw new UserStoreException("SQL statement for check existing user is null");
		boolean isExisting = false;
		isExisting = isValueExisting(sqlStmt, (Connection)null, new Object[] {userName});
		return isExisting;
//		return true;
	}

	@Override
	public Properties getDefaultUserStoreProperties(){
		Properties properties = new Properties();
		properties.setMandatoryProperties(AVHCustomUserStoreManagerConstants.CUSTOM_UM_MANDATORY_PROPERTIES.toArray
				(new Property[AVHCustomUserStoreManagerConstants.CUSTOM_UM_MANDATORY_PROPERTIES.size()]));
		properties.setOptionalProperties(AVHCustomUserStoreManagerConstants.CUSTOM_UM_OPTIONAL_PROPERTIES.toArray
				(new Property[AVHCustomUserStoreManagerConstants.CUSTOM_UM_OPTIONAL_PROPERTIES.size()]));
		properties.setAdvancedProperties(AVHCustomUserStoreManagerConstants.CUSTOM_UM_ADVANCED_PROPERTIES.toArray
				(new Property[AVHCustomUserStoreManagerConstants.CUSTOM_UM_ADVANCED_PROPERTIES.size()]));

		return properties;
	}
}