package de.measite.gitserve.sshd.git

import org.apache.sshd.server.FileSystemFactory
import org.apache.sshd.common.Session

class RepositoryFileSystemFactory extends FileSystemFactory {

    override def createFileSystemView(session : Session) =
        new RepositoryFileSystemView(session)

}
