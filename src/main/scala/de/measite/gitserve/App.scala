package de.measite.gitserve
import java.util.UUID
import java.io._
import scala.collection.JavaConversions._

object App {

    def copy(src : File, dst : File) {
        if (src.isDirectory) {
            if (!dst.exists) {
                dst.mkdir
                dst.deleteOnExit
            }
            src.list.foreach(f => {
                if (!List(".", "..").contains(f)) {
                    copy(new File(src, f), new File(dst, f))
                }
            })
        } else if (src.isFile) {
            val in = new FileInputStream(src)
            val out = new FileOutputStream(dst)
            val inc = in.getChannel
            val outc = out.getChannel
            inc.transferTo(0, Long.MaxValue, outc)
            dst.deleteOnExit
        }
    }

    def main(args : Array[String]) : Unit = {
        val uuid = UUID.randomUUID
        val tmpFile = if (new File("/tmp").exists) {
            new File("/tmp/gitserve-" + uuid)
        } else {
            new File("./.gitserve-" + uuid)
        }
        val (serveDir, repoDir) = if (new File(".git").exists) {
            val checkout = new File(".git").list.filter(f =>
                !List(".", "..", ".git").contains(f)
            ).size > 0
            if (checkout) {
                (tmpFile, new File(".git"))
            } else {
                (new File(".git"), new File(".git"))
            }
        } else {
            (new File("."), new File("."))
        }
        if (serveDir != repoDir) {
            System.out.println("preparing " + serveDir + " as your git repository")
            serveDir.mkdir
            copy(repoDir, serveDir)
            System.out.println("THIS REPOSITORY WILL BE REMOVED ON JVM SHUTDOWN")
        }
        new sshd.git.Server(serveDir)
    }

}
