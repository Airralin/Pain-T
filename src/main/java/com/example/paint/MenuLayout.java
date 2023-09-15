package com.example.paint;


import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.io.*;


public class MenuLayout {

    final MenuItem Save, SaveAs, Open,releaseNotes, About;
    private final MenuBar menuBar;

    final Menu File, Help;

    private final FileChooser pickAFile;

    private File saveFile;

    private File releaseNotesFile = new File("/Users/alkra/IdeaProjects/PAIN-T/src/main/resources/com/example/paint/ReleaseNotes/ReleaseNotesPain-T.txt");



    public MenuLayout(){


        File = new Menu("File");
        Help = new Menu("Help");

        menuBar = new MenuBar();
        Save = new MenuItem("Save");
        SaveAs = new MenuItem("Save As");
        Open = new MenuItem("Open");
        releaseNotes = new MenuItem("Release Notes");
        About = new MenuItem("About");




        Stage homeStage = main.getMyStage();
        //Creates menu Bar
        menuBar.getMenus().add(File);
        menuBar.getMenus().add(Help);

        //Adds items to File
        File.getItems().add(Save);

        File.getItems().add(SaveAs);
        File.getItems().add(Open);

        pickAFile = new FileChooser();

        Help.getItems().add(releaseNotes);
        Help.getItems().add(About);

        Save.setOnAction(e -> {
            if(saveFile == null){
                try {
                    saveImageAs( pickAFile, Drawing.getNewProject(), homeStage);

                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
            else{
                try {
                    saveImage(saveFile, Drawing.getNewProject());

                } catch (IOException ex) {
                    // handle exception...
                }
            }

        });



        SaveAs.setOnAction(q -> {
            try {
                saveImageAs( pickAFile, Drawing.getNewProject(), homeStage);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        Open.setOnAction(t -> {
            openImage(saveFile, pickAFile, Drawing.getNewProject(), homeStage);
        });

        releaseNotes.setOnAction(w -> {
            windowWithDialog(releaseNotesFile);
        });



    }

    public void windowWithDialog(File f){
        Stage DialogStage = new Stage();


        TextArea ta = new TextArea();
        Scene scene = new Scene(ta);

        System.setOut(new PrintStream(new OutputStream() {

            @Override
            public void write(int b) throws IOException {
                ta.appendText("" + ((char) b));
            }

            @Override
            public void write(byte[] b) throws IOException {
                ta.appendText(new String(b));
            }

            @Override
            public void write(byte[] b, int off, int len) throws IOException {
                ta.appendText(new String(b, off, len));
            }
        }));

        DialogStage.setScene(scene);
        DialogStage.show();
        FileReader fr;
        try {
            fr = new FileReader(f);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        int i;
        // Holds true till there is nothing to read
        while (true)

        // Print all the content of a file
        {
            try {
                if (!((i = fr.read()) != -1)) break;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.out.print((char) i);
        }
    }




    public void saveImageAs(FileChooser pickIt, Canvas canvas, Stage stage) throws IOException {
        saveFile = pickIt.showSaveDialog(stage);
        saveImage(saveFile, canvas);
    }



    public void saveImage(File file, Canvas canvas) throws IOException {
        Image image = getRegion(canvas, canvas.getWidth(), canvas.getHeight());

        ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
    }

    public Image getRegion(Canvas canvas, double width, double height){


        SnapshotParameters sp = new SnapshotParameters();
        //writes image
        WritableImage writtenImage = new WritableImage((int)width, (int)height);

        canvas.snapshot(sp, writtenImage);
        return writtenImage;
    }

    public void openImage(File file, FileChooser fc, Canvas canvas, Stage stage){
        if(file == null)
            file = fc.showOpenDialog(stage);
        main.drawImage(file, canvas);
    }

    public MenuBar getMenuBar(){
        return menuBar;
    }

}
