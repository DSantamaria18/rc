/**
 * Created by David on 20/08/2016.
 */
import com.jcraft.jsch.JSch
import com.jcraft.jsch.Session
import groovy.sql.Sql

//import org.jsoup

enum Env {
    QA("vpc-qa.ticketbis.com", "ec2-user", "qa-dbmaster-001.qa.vpc", "root", "938fdjsjg84hjfhowfe", "com.mysql.jdbc.Driver"),
    TEST("tb-pruebas-front007.aws.evandti.com", "ubuntu", "tb-pruebas-dbmaster002.cyfyh2whtxl0.eu-west-1.rds.amazonaws.com", "dev", "PzdFWxj3", "com.mysql.jdbc.Driver"),

    public final String bastionHost, bastionUser, localHost, remoteHost, user, pass, driver
    public int localPort, remotePort
    //private
    Session session

    public Env(String bHost, String bUser, String rHost, String dbUser, String dbPass, String dbDriver) {
        bastionHost = bHost
        bastionUser = bUser
        localHost = "localhost"
        localPort = 0
        remoteHost = rHost
        remotePort = 3306
        user = dbUser
        pass = dbPass
        driver = dbDriver
        session =  createSession()
    }

    //private
    Session createSession() {
        try {
            String privateKey = 'C:\\Users\\Roberto\\Documents\\Keys\\openssh_nopass.key'
            //String privateKey = 'C:\\Users\\David\\Documents\\Ticketbis\\Settings\\openssh_nopass.key'
            Properties config = new Properties()
            config.put("StrictHostKeyChecking", "no")
            JSch jsch = new JSch()
            jsch.addIdentity(privateKey)
            Session sshSession = jsch.getSession(bastionUser, bastionHost)
            sshSession.setConfig(config)
            sshSession.connect()
            def assignedPort = sshSession.setPortForwardingL(localPort, remoteHost, remotePort)
            localPort = assignedPort
            return session
        } catch (Exception ex) {
            println ex.message
        }
    }
}

class Environment {
    private Sql sql
    static Env env
    private static Environment singleEnvironment

    private Environment(params) {
        env = (params?.environment) ?: Env.QA
    }

    public static getInstance() {
        return singleEnvironment
    }

    public static createInstance(params) {
        if (singleEnvironment == null) {
            singleEnvironment = new Environment(params)
            return singleEnvironment
        } else {
            // Exception
        }
    }

    def connectDb() {
        def host = env.localHost + ":" + env.localPort
        return sql = Sql.newInstance("jdbc:mysql://${host}/ticketbis", env.user, env.pass, env.driver)
    }

    def getQuery(String query) {
        def conn = connectDb()
        try {
            return conn.rows(query)
        } catch (Exception ex) {
            println(ex.message)
        } finally {
            conn.close()
        }
    }
}
