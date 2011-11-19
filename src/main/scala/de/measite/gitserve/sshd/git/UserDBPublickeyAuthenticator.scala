package de.measite.gitserve.sshd.git

import org.apache.sshd.server.PublickeyAuthenticator
import java.security.PublicKey
import java.security.interfaces.RSAPublicKey
import java.io.ByteArrayOutputStream
import org.apache.sshd.server.session.ServerSession

class UserDBPublickeyAuthenticator extends PublickeyAuthenticator {

    override def authenticate(
        username : String,
        key : PublicKey,
        session: ServerSession
    ) : Boolean = true

}