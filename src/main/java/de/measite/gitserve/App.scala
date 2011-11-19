package de.measite.gitserve

object App {

    def main(args : Array[String]) : Unit = {
        sshd.git.Server.started
    }

}
