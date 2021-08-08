import java.awt.DisplayMode;
import java.awt.GraphicsEnvironment;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * The class is responsible for all the functionality of the application.
 */
public class Drawing {

    /**
     * The X-coordinate of the upper-left corner of the future rectangle.
     */
    private Integer topLeftCornerX;

    /**
     * The Y-coordinate of the upper-left corner of the future rectangle.
     */
    private Integer topLeftCornerY;

    /**
     * The X-coordinate of the lower-right corner of the future rectangle.
     */
    private Integer bottomRightCornerX;

    /**
     * The Y-coordinate of the lower-right corner of the future rectangle.
     */
    private Integer bottomRightCornerY;

    /**
     * A list that contains the rectangles we have drawn.
     */
    private final List<Rectangle> listOfDrawnRectangles = new ArrayList<>();

    /**
     * Constructor that performs all operations.
     */
    public Drawing() {

        // Declaration of variables that we will need in the process.
        Display display = new Display();
        Shell windowOfOurApplication = new Shell(display);
        DisplayMode userMainScreen = GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getDefaultScreenDevice().getDisplayMode();
        Canvas surfaceForDrawing = new Canvas(windowOfOurApplication, SWT.CENTER);
        GC drawingCoordinateSystem = new GC(surfaceForDrawing);
        Button allowDraw = new org.eclipse.swt.widgets.Button(surfaceForDrawing, SWT.TOGGLE);

        // Preparing variables that we will need in the process of work.
        prepareEnvironment(windowOfOurApplication, userMainScreen, surfaceForDrawing, allowDraw);

        drawing(surfaceForDrawing, allowDraw, drawingCoordinateSystem);

        while (!windowOfOurApplication.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        surfaceForDrawing.dispose();
        display.dispose();
    }

    /**
     * The method is responsible for all the functionality that we need.
     *
     * @param canvas                  - surface for drawing arbitrary graphics.
     * @param button                  - the button responsible for allowing or prohibiting drawing.
     * @param drawingCoordinateSystem - drawing coordinate system is the two-dimensional space.
     */
    private void drawing(final Canvas canvas, final Button button, final GC drawingCoordinateSystem) {
        canvas.addMouseListener(new MouseListener() {
            @Override
            public void mouseDoubleClick(MouseEvent mouseEventDoubleClick) {
            }

            @Override
            public void mouseDown(MouseEvent mouseEventDown) {
                topLeftCornerX = mouseEventDown.x;
                topLeftCornerY = mouseEventDown.y;
            }

            @Override
            public void mouseUp(MouseEvent mouseEventUp) {
                if (button.getSelection()) {
                    createRectangle(mouseEventUp, drawingCoordinateSystem);
                }
            }
        });
    }

    /**
     * A method that draws a rectangle and puts it on the list.
     *
     * @param mouseEvent              - mouse related actions.
     * @param drawingCoordinateSystem - drawing coordinate system is the two-dimensional space.
     */
    private void createRectangle(final MouseEvent mouseEvent, final GC drawingCoordinateSystem) {
        bottomRightCornerX = mouseEvent.x;
        bottomRightCornerY = mouseEvent.y;
        controlAndChangeRectangle();
        int width = bottomRightCornerX - topLeftCornerX;
        int height = bottomRightCornerY - topLeftCornerY;
        for (Rectangle alreadyDrawnRectangle : listOfDrawnRectangles) {
            if (alreadyDrawnRectangle.intersects(topLeftCornerX, topLeftCornerY, width, height)) {
                return;
            }
        }
        Rectangle newRectangle = new Rectangle(topLeftCornerX, topLeftCornerY, width, height);
        drawingCoordinateSystem.setBackground(mouseEvent.display.getSystemColor(SWT.COLOR_BLACK));
        drawingCoordinateSystem.drawRectangle(newRectangle);
        drawingCoordinateSystem.fillRectangle(newRectangle);
        listOfDrawnRectangles.add(newRectangle);
    }

    /**
     * Method responsible for setting variables.
     *
     * @param windowOfOurApplication - our application window.
     * @param userMainScreen         - main display of user.
     * @param surfaceForDrawing      - surface for drawing.
     * @param allowDraw              - a button that allows or denies drawing.
     */
    private void prepareEnvironment(final Shell windowOfOurApplication, final DisplayMode userMainScreen,
                                    final Canvas surfaceForDrawing, final Button allowDraw) {
        allowDraw.setBounds(10, 10, 150, 40);
        allowDraw.setText("Start drawing");
        allowDraw.setLocation(userMainScreen.getWidth() / 2 - 150 / 2, 0);
        windowOfOurApplication.setText("Test for MedUni");
        windowOfOurApplication.setSize(userMainScreen.getWidth(), userMainScreen.getHeight());
        windowOfOurApplication.setLocation(0, 0);
        windowOfOurApplication.open();
        surfaceForDrawing.setLocation(windowOfOurApplication.getLocation());
        surfaceForDrawing.setSize(windowOfOurApplication.getSize());
        surfaceForDrawing.getChildren();
    }

    /**
     * A method that changes the coordinates of the rectangle we are about to draw.
     * This is necessary because the intersects() method only works if we set the points correctly.
     * The first is the top-left corner and the second is the bottom-right corner
     * (that is, topLeftCornerX < bottomRightCornerX and topLeftCornerY > bottomRightCornerY).
     * If this condition is not met, we change the coordinates of the points.
     */
    private void controlAndChangeRectangle() {
        if (this.topLeftCornerX < this.bottomRightCornerX && this.topLeftCornerY < this.bottomRightCornerY) {
            return;
        } else if (this.topLeftCornerX > this.bottomRightCornerX && this.topLeftCornerY > this.bottomRightCornerY) {
            Integer i = this.topLeftCornerX;
            this.topLeftCornerX = bottomRightCornerX;
            this.bottomRightCornerX = i;
            Integer yi = this.topLeftCornerY;
            this.topLeftCornerY = this.bottomRightCornerY;
            this.bottomRightCornerY = yi;
        } else if (this.topLeftCornerX > this.bottomRightCornerX && this.topLeftCornerY < this.bottomRightCornerY) {
            Integer xi = this.topLeftCornerX;
            this.topLeftCornerX = this.bottomRightCornerX;
            this.bottomRightCornerX = this.topLeftCornerX;
        } else if (this.topLeftCornerX < this.bottomRightCornerX && this.topLeftCornerY > this.bottomRightCornerY) {
            Integer yi = this.topLeftCornerY;
            this.topLeftCornerY = this.bottomRightCornerY;
            this.bottomRightCornerY = yi;
        }
    }
}
