package de.measite.gitserve.sshd.git
import org.apache.sshd.server.PasswordAuthenticator
import org.apache.sshd.server.session.ServerSession

class AnyPasswordAuth extends PasswordAuthenticator {

    override def authenticate(u : String, p : String, s : ServerSession) = true

}