package org.soyaga.examples.SlitherLink.ACO.Stats;

import org.soyaga.aco.Colony;
import org.soyaga.aco.Solution;
import org.soyaga.aco.StatsRetrievalPolicy.Stat.Stat;
import org.soyaga.aco.world.Graph.Elements.Edge;
import org.soyaga.aco.world.Graph.Elements.Node;
import org.soyaga.aco.world.World;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * This class implements the Stat interface. The plottable and writable information in this stat contains no data.
 * While executing the stat, an image is generated and stored in an internal variable. Later, the optimizer's
 * getResults() method will collect this array of images and create a GIF.
 */
public class ImageStat extends JPanel implements Stat {
    private final HashMap<Node,int[]> nodePoints;
    private final Node[][] nodes;
    private final double radius;
    private final int space;
    private BufferedImage image;
    private final BufferedImage baseImage;
    private final Integer imageWidth;
    private final Integer imageHeight;
    private final JFrame frame;
    private final double maxPheromone;

    /**
     * Constructor.
     */
    public ImageStat(Node[][] nodes, int rows, int cols, double maxPheromone) {
        this.nodePoints = new HashMap<>();
        this.nodes = nodes;
        this.radius = 10;
        this.space = 50;
        this.maxPheromone = 2*maxPheromone;
        this.frame = new JFrame("BufferedImage Plot");
        this.imageWidth = (cols+2) * this.space;
        this.imageHeight = (rows +2) * this.space;
        this.baseImage = creteInitialImage();
        plotImage();
        this.setImage(this.baseImage);
    }

    private void plotImage() {
        SwingUtilities.invokeLater(() -> {
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(this);
            frame.setLocation(50,350);
            frame.setSize(this.imageWidth, this.imageHeight);
            frame.setVisible(true);
        });
    }
    public void close(){
        this.frame.dispose();
    }

    /**
     * ArrayList of strings that compose the header for this stat.
     *
     * @return ArrayList{@literal <String>} with the column names.
     */
    @Override
    public ArrayList<String> getHeader() {
        return new ArrayList<>(){{add("ImageSaved");}};
    }

    /**
     * ArrayList of strings that compose the values for this stat.
     *
     * @param world    world used to measure the stats.
     * @param colony   Colony used to measure the stats.
     * @param statArgs VarArgs containing the additional information needed to apply the stat. The generation number in this case.
     * @return ArrayList{@literal <String>} with the column names.
     */
    @Override
    public ArrayList<String> apply(World world, Colony colony, Object... statArgs) {
        this.setImage((BufferedImage) this.getImage(colony, (Integer) statArgs[0]));
        return new ArrayList<>(){{add("true");}};
    }

    /**
     * Function that computes the new image.
     *
     * @param colony Colony used to measure the stats.
     * @param generation Integer with the generation.
     * @return the image of the new solution.
     */
    private Image getImage(Colony colony, Integer generation){
        Solution solution = colony.getBestSolution();
        BufferedImage image = deepCopy(this.baseImage);
        Graphics2D graphics2D = image.createGraphics();
        graphics2D.setColor(Color.WHITE);
        graphics2D.setStroke(new BasicStroke((float) this.imageHeight /100));
        for(Node[] nodeCol:this.nodes) {
            for (Node node : nodeCol) {
                int[] origin= this.nodePoints.get(node);
                for(Edge edge:node.getOutputEdges()){
                    int[] dest= this.nodePoints.get(edge.getDestination());
                    graphics2D.setStroke(
                            new BasicStroke(
                                    (float) (2*this.radius*Math.pow(edge.getPheromone()/this.maxPheromone,0.4))
                            )
                    );
                    graphics2D.setColor(
                            new Color(
                                    255,255,255,
                                    (int) (255*Math.pow(edge.getPheromone()/this.maxPheromone,0.4))
                            )
                    );
                    graphics2D.drawLine(
                            origin[0],
                            origin[1],
                            dest[0],
                            dest[1]
                    );
                }
            }
        }
        graphics2D.setColor(Color.RED);
        graphics2D.setColor(Color.GREEN);
        graphics2D.setFont(new Font(graphics2D.getFont().getName(),graphics2D.getFont().getStyle(),this.imageHeight/25));
        graphics2D.drawString("Fitness= "+solution.getFitnessValue() + ", Generation= "+ generation,graphics2D.getFont().getSize(),graphics2D.getFont().getSize());
        graphics2D.dispose();
        return image;
    }

    /**
     * Function that creates and return a copy of a buffered image.
     * @param bi BufferedImage to copy.
     * @return BufferedImage with a deep copy.
     */
    static BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }

    /**
     * Function that stores the first images:
     * <ol>
     *     <li>Only nodes * 2 times.</li>
     *     <li>(Nodes + all edges) * 5 times.</li>
     * </ol>
     */
    private BufferedImage creteInitialImage() {
        BufferedImage image = new BufferedImage(this.imageWidth, this.imageHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = image.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        graphics2D.setBackground(Color.DARK_GRAY);
        graphics2D.clearRect(0, 0, imageWidth, imageHeight);
        graphics2D.setColor(Color.WHITE);
        int row=1;
        for(Node[] nodeCol:this.nodes){
            int col = 1;
            for(Node node:nodeCol){
                int nodex= col*this.space;
                int nodey= row*this.space;
                this.nodePoints.put(node,new int[]{(int) (nodex+this.radius),(int) (nodey+this.radius)});
                Shape shape = new Ellipse2D.Double(
                        nodex,
                        nodey,
                        2*this.radius,
                        2*this.radius);
                graphics2D.fill(shape);
                col++;
            }
            row++;
        }
        graphics2D.dispose();
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            g.drawImage(image, 0, 0, null);
        }
    }
}
