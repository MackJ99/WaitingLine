import java.util.HashMap;
import java.util.Map;


public class Main {

	public static void main(String[] args) {
		
		
		String IV = "1001";
		String text = "101100101100";
		
	// codebook with 16 possible combinations of 4x4. all have to be unique and cannot be repeated.
		Map<String, String> codebook = new HashMap<>();
	    codebook.put("0000", "1001");
        codebook.put("0001", "1010");
        codebook.put("0010", "1011");
        codebook.put("0011", "1100");
        codebook.put("0100", "1101");
        codebook.put("0101", "1110");
        codebook.put("0110", "1111");
        codebook.put("0111", "0000");
        codebook.put("1000", "0001");
        codebook.put("1001", "0010");
        codebook.put("1010", "0011");
        codebook.put("1011", "0100");
        codebook.put("1100", "0101");
        codebook.put("1101", "0110");
        codebook.put("1110", "0111");
        codebook.put("1111", "1000");
		
		
		
		
     // playing the game
        String previousStrategy = IV;
        StringBuilder ciphertext = new StringBuilder();
     // block loop   
        for (int i = 0; i < 3; i++) {
     // strings for the blocks and xor 
            String section = text.substring(i*4, (i+1)*4);
            String outcome = xor(previousStrategy, section);
            String nextPlay = codebook.get(outcome);
            
            ciphertext.append(nextPlay);
            previousStrategy = nextPlay;
        }
        //printing the encrypted value
        System.out.println("Ciphertext: " + ciphertext.toString());
    }

    // combining strategies. plaintext block xor with previous block of cipher. use length a bc and b are same length. 
    public static String xor(String a, String b) {
        StringBuilder result = new StringBuilder();
    // xor operation.
        for (int i = 0; i < a.length(); i++) {
            result.append(a.charAt(i) == b.charAt(i) ? '0' : '1');
        }
        return result.toString();
    }
}






