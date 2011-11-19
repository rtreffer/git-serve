package de.measite.gitserve.sshd.git

import java.util.Arrays
import java.io.File
import collection.JavaConversions._
import org.apache.sshd.SshServer
import org.apache.sshd.server.kex._
import org.apache.sshd.common.mac._
import org.apache.sshd.common._
import org.apache.sshd.common.Cipher
import org.apache.sshd.common.random.SingletonRandomFactory
import org.apache.sshd.common.random.BouncyCastleRandom
import org.apache.sshd.common.cipher._
import org.apache.sshd.common.compression._
import org.apache.sshd.server.channel.ChannelSession
import org.apache.sshd.server.channel.ChannelDirectTcpip
import org.apache.sshd.common.signature.SignatureDSA
import org.apache.sshd.common.signature.SignatureRSA
import org.apache.sshd.server.keyprovider.PEMGeneratorHostKeyProvider;
import org.apache.sshd.common.NamedFactory
import org.bouncycastle.jce.provider.BouncyCastleProvider

object Server {

    java.security.Security.insertProviderAt(new BouncyCastleProvider, 0)

    val sshd = new SshServer()

    // Security settings
    sshd.setKeyExchangeFactories(List(
        new DHG1.Factory(),
        new DHG14.Factory()
    ))
    sshd.setRandomFactory(
        new SingletonRandomFactory(new BouncyCastleRandom.Factory())
    );
    val sshDir = new File(new File(System.getProperty("user.home", ".")), ".ssh")
    if (!sshDir.exists) {
        sshDir.mkdir
    }
    sshd.setKeyPairProvider(new PEMGeneratorHostKeyProvider(
        new File(sshDir, "ssh-serve-key.pem").toString));

    sshd.setCompressionFactories(List(
        new CompressionZlib.Factory(),
        new CompressionNone.Factory(),
        new CompressionDelayedZlib.Factory())
    );

    sshd.setMacFactories(List(
        new HMACMD5.Factory(),
        new HMACSHA1.Factory(),
        new HMACMD596.Factory(),
        new HMACSHA196.Factory()
    ));
    sshd.setChannelFactories(List(
        new ChannelSession.Factory(),
        new ChannelDirectTcpip.Factory()
    ));
    sshd.setSignatureFactories(List(
        new SignatureDSA.Factory(),
        new SignatureRSA.Factory()
    ));

    setUpDefaultCiphers(sshd)

    sshd.setPasswordAuthenticator(new AnyPasswordAuth)
    sshd.setPublickeyAuthenticator(new UserDBPublickeyAuthenticator)
    sshd.setFileSystemFactory(new RepositoryFileSystemFactory)
    sshd.setCommandFactory(new GitCommandFactory)

    sshd.setPort(2222)
    sshd.start

    def started = true

    def setUpDefaultCiphers(sshd : SshServer) = {
        val ciphers = List(
            new AES128CBC.Factory(),
            new TripleDESCBC.Factory(),
            new BlowfishCBC.Factory(),
            new AES192CBC.Factory(),
            new AES256CBC.Factory()
        )

        val workingCiphers = ciphers.filter(factory => {
            try {
                val cipher = factory.create
                cipher.init(
                    Cipher.Mode.Encrypt,
                    new Array[Byte](cipher.getBlockSize),
                    new Array[Byte](cipher.getIVSize)
                )
                true
            } catch {
                case e => e.printStackTrace; false
            }
        })

        System.out.println("sshd ciphers: " +
            workingCiphers.map(c => c.getName()).mkString(","))
        sshd.setCipherFactories(workingCiphers);
    }

    def shutdown = sshd.stop

    System.out.println("""
   SSHD started on localhost:2222
   use "git+ssh://localhost:2222/" as your push url
""")
}