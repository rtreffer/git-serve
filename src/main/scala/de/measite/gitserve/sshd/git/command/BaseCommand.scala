package de.measite.gitserve.sshd.git.command

import org.apache.sshd.server.Command
import org.apache.sshd.server.Environment
import org.apache.sshd.server.ExitCallback
import org.apache.sshd.server.SessionAware
import org.apache.sshd.server.session.ServerSession
import java.io.InputStream
import java.io.OutputStream

abstract class BaseCommand
extends Command
with SessionAware {

    var in : InputStream = null
    var out : OutputStream = null
    var err : OutputStream = null
    var callback : ExitCallback = null
    var username : String = null

    override def destroy = {
        try { in.close } catch {case _ => {}}
        try { out.flush } catch {case _ => {}}
        try { out.close } catch {case _ => {}}
        try { err.flush } catch {case _ => {}}
        try { err.close } catch {case _ => {}}
    }

    override def setInputStream(in : InputStream) = {
        this.in = in;
    }

    override def setOutputStream(out : OutputStream) = {
        this.out = out;
    }

    override def setErrorStream(err : OutputStream) = {
        this.err = err;
    }

    override def setExitCallback(callback : ExitCallback) = {
        this.callback = callback;
    }

    override def setSession(session : ServerSession) = {
        username = session.getUsername
    }

}