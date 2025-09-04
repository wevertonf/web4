package util;

import org.mindrot.jbcrypt.BCrypt;

public class SenhaUtil {
	//receber a senha normal e devolver criptografada
	public static String hashSenha(String senhaPura) {
		return BCrypt.hashpw(senhaPura, BCrypt.gensalt());
	}
	
	public static boolean verificarSenha(String senhaForm, String senhaBanco) {
		return BCrypt.checkpw(senhaForm, senhaBanco);
	}

}
