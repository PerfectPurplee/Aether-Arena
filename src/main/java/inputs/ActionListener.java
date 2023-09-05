package inputs;


import java.awt.event.ActionEvent;
import static scenes.AllScenes.*;

public class ActionListener implements java.awt.event.ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {

        switch (Current_Scene) {

            case PLAYING -> {
            }
            case MENU -> {


            }
            case PAUSE -> {
            }
            case MAP_EDITOR -> {
            }
        }

    }
}
