import Frames.MainFrame;
import Frames.SignInFrame;
import Utilities.EntryHandler;
import Utilities.GoogleAuth;

public class Main {
    public static void main(String[] args) {
        try {
            EntryHandler.initialize();
        } catch (Exception e) {
            e.printStackTrace();
        }

        SignInFrame frame = new SignInFrame();
        // Check if already authenticated
        if (GoogleAuth.isAuthenticated()) {
            // Validate auth
            MainFrame mainFrame = new MainFrame(frame);
            GoogleAuth.init(new GoogleAuth.Listener() {
                @Override
                public void onSuccess() {
                    System.out.println("Success");
                    // Directly start Mainframe
                    frame.pushNext(mainFrame, false);
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