import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class WktEditor {

    JLabel selectedMode = new JLabel();
    String[] modeString=new String[]{"Line","Point","Polygon"};
    int mode = 0;
    TestPane tpane = new TestPane();

    ArrayList<Point> wkt_points=new ArrayList<>();
    ArrayList<Line> wkt_lines=new ArrayList<>();
    ArrayList<Polygon> wkt_polygon=new ArrayList<>();
    Line line = null;
    Polygon polygon = null;

    public WktEditor(){
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        tpane.setPreferredSize(new Dimension(200,200));
        tpane.setBackground(new Color(176, 255, 121));
        JPanel buttonPanel = new JPanel();
        JButton lineButton = new JButton("Line");
        JButton pointButton = new JButton("Point");
        JButton polygonButton = new JButton("Polygon");
        JButton convertButton = new JButton("Convert to WKT");

        lineButton.setFocusable(false);
        pointButton.setFocusable(false);
        polygonButton.setFocusable(false);
        convertButton.setFocusable(false);
        lineButton.addActionListener(actionEvent -> {
            mode = 0;
            setMode();
        });
        pointButton.addActionListener(actionEvent -> {
            mode = 1;
            setMode();
        });
        polygonButton.addActionListener(actionEvent -> {
            mode = 2;
            setMode();
        });
        convertButton.addActionListener(actionEvent -> {
            convertToWKT();
        });

        buttonPanel.add(lineButton);
        buttonPanel.add(pointButton);
        buttonPanel.add(polygonButton);
        buttonPanel.add(selectedMode);
        buttonPanel.add(convertButton);
        buttonPanel.setFocusable(false);
        this.setMode();

        tpane.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                WktEditor.this.addPoint(mouseEvent.getX(),mouseEvent.getY());
            }

            @Override
            public void mousePressed(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseEntered(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseExited(MouseEvent mouseEvent) {

            }
        });

        frame.setFocusable(true);
        frame.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {
            }

            @Override
            public void keyPressed(KeyEvent keyEvent) {

            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {
                if(keyEvent.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    switch (WktEditor.this.mode)
                    {
                        case 0:
                            WktEditor.this.wkt_lines.add(new Line());
                            break;
                        case 1:
                            break;
                        case 2:
                            WktEditor.this.wkt_polygon.add(new Polygon());
                            break;
                    }
                }
                WktEditor.this.tpane.repaint();
            }
        });

        frame.getContentPane().add(tpane, BorderLayout.CENTER);
        frame.getContentPane().add(buttonPanel, BorderLayout.NORTH);
        frame.pack();
        frame.setVisible(true);
    }

    private void convertToWKT() {
        JFrame end_frame = new JFrame("WKT output");
        String wkt_string="";
        for(Point p : wkt_points)
        {
            wkt_string += p.toString() +"\n";
            System.out.println(p.toString());
        }
        System.out.println();
        for(Line l : wkt_lines)
        {
            wkt_string += l.toString() +"\n";
            System.out.println(l.toString());
        }
        System.out.println();
        for(Polygon p : wkt_polygon)
        {
            wkt_string += p.toString() +"\n";
            System.out.println(p.toString());
        }
        System.out.println();
        JTextArea label = new JTextArea(wkt_string);
        label.selectAll();
        label.copy();
        end_frame.setVisible(true);
        end_frame.getContentPane().add(label, BorderLayout.CENTER);
        end_frame.pack();
    }

    public void setMode(){
        selectedMode.setText(modeString[mode]);
    }

    public void addPoint(int x, int y){

        switch (mode)
        {
            case 0:
                if(line == null)
                {
                    line = new Line();
                    wkt_lines.add(line);
                }
                wkt_lines.get(wkt_lines.size()-1).addPoint(new Point(x,y));
                tpane.addLines(wkt_lines);
                break;
            case 1:
                wkt_points.add(new Point(x,y));
                tpane.addPoints(wkt_points);
                break;
            case 2:
                if(polygon == null)
                {
                    polygon = new Polygon();
                    wkt_polygon.add(polygon);
                }
                wkt_polygon.get(wkt_polygon.size()-1).addPoint(new Point(x,y));
                tpane.addPolygons(wkt_polygon);
                break;
        }
    }

    private class TestPane extends JPanel {

        int width = 1512;
        int height = 1007;
        Image img=null;

        public TestPane(){
            File imgFile = new File("resources/Uni.png");
            try {
                img = ImageIO.read(imgFile);
                width = (int) (((BufferedImage) img).getWidth() * 0.9);
                height = (int) (((BufferedImage) img).getHeight() * 0.9);
                img = img.getScaledInstance(width,height,Image.SCALE_SMOOTH);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        ArrayList<Point> point_list=new ArrayList<>();
        ArrayList<Line> line_list = new ArrayList<>();
        ArrayList<Polygon> polygon_list = new ArrayList<>();

        @Override
        public Dimension getPreferredSize(){
            return new Dimension(width,height);
        }


        @Override
        protected void paintComponent(Graphics g){
            super.paintComponent(g);
            int diameter = 10;
            int radius = 5;
            if(img != null){
                g.drawImage(img,0,0,null);
            }
            for(Point point:point_list){
                g.fillOval(point.getX()-radius,point.getY()-radius,diameter,diameter);
            }
            for(int pos=0;pos<line_list.size();pos++){
                Line line = line_list.get(pos);
                if (pos+1 == line_list.size())
                    g.setColor(new Color(255, 105, 5));
                for(int i=0; i < line.getSize(); i++){
                    Point[] tmp_points = line.getPoints(i);
                    if(tmp_points != null){
                        if(tmp_points.length > 0) {
                            if(tmp_points.length > 1){
                                g.drawLine(tmp_points[0].getX(),tmp_points[0].getY(),tmp_points[1].getX(),tmp_points[1].getY());
                                g.fillOval(tmp_points[1].getX()-radius,tmp_points[1].getY()-radius,diameter,diameter);
                            }
                            g.fillOval(tmp_points[0].getX()-radius,tmp_points[0].getY()-radius,diameter,diameter);
                        }
                    }
                }
                g.setColor(new Color(0,0,0));
            }
            for(int i=0;i<polygon_list.size();i++){
                Polygon polygon=polygon_list.get(i);
                if (i+1 == polygon_list.size())
                    g.setColor(new Color(255, 105, 5));
                for(Point p:polygon.getPoints())
                {
                    g.fillOval(p.getX()-radius,p.getY()-radius,diameter,diameter);
                }
                g.drawPolygon(polygon.getXs(),polygon.getYs(),polygon.getSize());
            }

        }

        public void addPoints(ArrayList<Point> points){
            point_list = points;
            this.repaint();
        }

        public void addLines(ArrayList<Line> lines) {
            line_list = lines;
            this.repaint();
        }

        public void addPolygons(ArrayList<Polygon> polygons) {
            polygon_list = polygons;
            this.repaint();
        }
    }

    public class Point {
        private int x;
        private int y;

        public Point(int x1, int y1){
            x=x1;
            y=y1;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public void setX(int x) {
            this.x = x;
        }

        public void setY(int y) {
            this.y = y;
        }

        public boolean equals(Point p) {
            return (x==p.getX())&&(y==p.getY());
        }

        @Override
        public String toString() {
            return "POINT ("+x+" "+y+")";
        }
        public String toValues() {
            return x+" "+y;
        }
    }

    private class Line {

        private ArrayList<Point> points=new ArrayList<>();

        public Line(){
        }

        public Line(Point p1, Point p2){
            points.add(p1);
            points.add(p2);
        }

        public Line(ArrayList<Point> p_list){
            points=p_list;
        }

        public void addPoint(Point p1){
            points.add(p1);
        }

        public int getSize(){
            return points.size();
        }

        public Point[] getPoints(int i){
            if(i == 0 && points.size() == 1)
                return new Point[]{points.get(0)};
            if(i+1 >= points.size() || i < 0)
                return null;
            return new Point[]{points.get(i),points.get(i+1)};
        }

        @Override
        public String toString() {
            String tmp = "LINESTRING (";
            tmp += points.stream().map(Point::toValues)
                    .collect(Collectors.joining(", "));
            return tmp+")";
        }
    }

    private class Polygon {

        private ArrayList<Point> points=new ArrayList<>();

        public Polygon(){
        }

        public Polygon(Point p1, Point p2){
            points.add(p1);
            points.add(p2);
        }

        public Polygon(ArrayList<Point> p_list){
            points=p_list;
        }

        public void addPoint(Point p1){
            points.add(p1);
        }

        public int[] getXs(){
            int[] tmp = new int[points.size()];
            for(int i=0; i< points.size(); i++){
                tmp[i] = points.get(i).getX();
            }
            return tmp;
        }

        public int[] getYs(){
            int[] tmp = new int[points.size()];
            for(int i=0; i< points.size(); i++){
                tmp[i] = points.get(i).getY();
            }
            return tmp;
        }

        public int getSize(){
            return points.size();
        }

        public ArrayList<Point> getPoints() {
            return points;
        }

        @Override
        public String toString() {
            String tmp = "POLYGON ((";
            tmp += points.stream().map(Point::toValues)
                    .collect(Collectors.joining(", "));
            if(points.size() >= 1)
                tmp += ", "+points.get(0).toValues();
            return tmp+"))";
        }
    }
}
