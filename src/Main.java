import Frames.MainFrame;
import Frames.SignInFrame;
import Utilities.EntryHandler;
import Utilities.GoogleAuth;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
        if (!Files.exists(Path.of(GoogleAuth.TOKENS_DIRECTORY_PATH + "/"))) {
            try {
                Files.createDirectories(Path.of(GoogleAuth.TOKENS_DIRECTORY_PATH + "/"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            EntryHandler.initialize();
        } catch (Exception e) {
            e.printStackTrace();
        }

        SignInFrame frame = new SignInFrame();
        // Check if already authenticated
        if (GoogleAuth.isAuthenticated()) {
            // Validate auth
            GoogleAuth.init(new GoogleAuth.Listener() {
                @Override
                public void onSuccess() {
                    System.out.println("Success");
                    // Directly start Mainframe
                    MainFrame mainFrame = new MainFrame();
                    mainFrame.display();
                }

                @Override
                public void onFailure(Exception e) {
                    System.out.println("Fail");
                    // Sign in failed
                    // Delete saved token
                    try {
                        GoogleAuth.signOut();
                    } catch (Exception e2) {}
                    frame.display();
                }
            });
        } else {
            // Prompt user to sign in
            frame.display();
        }
    }
}