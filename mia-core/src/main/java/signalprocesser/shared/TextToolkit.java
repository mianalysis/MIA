/*
 * "Concave" hulls by Glenn Hudson and Matt Duckham
 *
 * Source code downloaded from https://archive.md/l3Un5#selection-571.0-587.218 on 3rd November 2021.
 *
 * - This software is Copyright (C) 2008 Glenn Hudson released under Gnu Public License (GPL). Under 
 *   GPL you are free to use, modify, and redistribute the software. Please acknowledge Glenn Hudson 
 *   and Matt Duckham as the source of this software if you do use or adapt the code in further research 
 *   or other work. For full details of GPL see http://www.gnu.org/licenses/gpl-3.0.txt.
 * - This software comes with no warranty of any kind, expressed or implied.
 * 
 * A paper with full details of the characteristic hulls algorithm is published in Pattern Recognition.
 * Duckham, M., Kulik, L., Worboys, M.F., Galton, A. (2008) Efficient generation of simple polygons for
 * characterizing the shape of a set of points in the plane. Pattern Recognition v41, 3224-3236
 *
 * The software was developed by Glenn Hudson while working with me as an RA. The characteristic shapes 
 * algorithm is collaborative work between Matt Duckham, Lars Kulik, Antony Galton, and Mike Worboys.
 * 
 */

package signalprocesser.shared;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

public class TextToolkit {
    
    /* ********************************************************* */
    // Constants
    
    private static final int   MAX_LINES = 10;
    private static final int   LENGTH_TO_CUTUP_WORDS = 5;
    private static final float DECREASE_FONT_HEIGHT_BY_FACTOR = 0.9f;
    
    private static final String TERMINATE_HALFWORD_WITH = "-";
    private static final String TERMINATE_IFMAXLEN_WITH = "...";
    
    private static final FontRenderContext fontrender = new FontRenderContext(null, true, true);
    
    
    /* ********************************************************* */
    // Variables
    
    // Note; as variables are shared for all methods, this makes the
    //  use of this toolkit single threaded, but this isn't a problem given
    //  there is only one sitemap and the paint() method is single threaded)
    
    private static String[] lineoftext = new String[MAX_LINES];

    
    /* ********************************************************* */
    // Public Methods
    
    // NOTE: the code in the writeFromTop(), writeFromLeft() and writeFromRight()
    //  methods are essentially duplicated for performance reasons.
     
    static public void writeFromTop(Graphics2D graphic, Font font, Color color, String text, Rectangle textbounds) {
        writeFromTop(graphic, font, color, text, textbounds, textbounds.height);
    }
     
    static public void writeFromTop(Graphics2D graphic, Font font, Color color, String text, Rectangle textbounds, int height) {
        
        // Get the metrics for the current font
        FontMetrics metrics = graphic.getFontMetrics(font);
        // The ascent is the actual height of the text, but decrease it
        //  by a certain factor to make the text displayed tighter together
        int fontheight = (int) ( metrics.getAscent() * DECREASE_FONT_HEIGHT_BY_FACTOR );
        
        // Determine the number of lines that can be displayed
        int numberoflines;
        int maxnumberoflines = height / fontheight;
        if ( maxnumberoflines<=1 ) {
            numberoflines = 1;
            getSinglelineOfText(text, metrics, textbounds.width);
        } else {
            if ( maxnumberoflines>MAX_LINES ) {
                maxnumberoflines = MAX_LINES;
            }
            numberoflines = getMultilineOfText(text, metrics, maxnumberoflines, textbounds.width);
        }
        
        // Print the text
        int margin = (height - numberoflines*fontheight)/2 + fontheight - metrics.getDescent()/2;
        for ( int line=0 ; line<numberoflines ; line++ ) {
            
            // Get text layout
            TextLayout textlayout = new TextLayout(lineoftext[line], font, fontrender);
            
            // Translate text layout
            Rectangle2D rectangle = textlayout.getBounds();
            AffineTransform transform = AffineTransform.getTranslateInstance( textbounds.x+(textbounds.width-rectangle.getWidth())/2 , textbounds.y+margin+fontheight*line );
            
            // Print text layout
            graphic.setPaint(color);
            if ( graphic.getRenderingHint(RenderingHints.KEY_ANTIALIASING)==RenderingHints.VALUE_ANTIALIAS_OFF ) {
                graphic.setRenderingHint(RenderingHints.KEY_ANTIALIASING     , RenderingHints.VALUE_ANTIALIAS_ON);
                graphic.fill( textlayout.getOutline(transform) );
                graphic.setRenderingHint(RenderingHints.KEY_ANTIALIASING     , RenderingHints.VALUE_ANTIALIAS_OFF);
            } else {
                graphic.fill( textlayout.getOutline(transform) );
            }
        }
    }
    
    static public void writeFromLeftCentered(Graphics2D graphic, Font font, Color color, String text, Rectangle textbounds) {
        
        // Get the metrics for the current font
        FontMetrics metrics = graphic.getFontMetrics(font);
        // The ascent is the actual height of the text, but decrease it
        //  by a certain factor to make the text displayed tighter together
        int fontheight = (int) ( metrics.getAscent() * DECREASE_FONT_HEIGHT_BY_FACTOR );
        
        // Determine the number of lines that can be displayed
        int numberoflines;
        int maxnumberoflines = textbounds.width / fontheight;
        if ( maxnumberoflines<=1 ) {
            numberoflines = 1;
            getSinglelineOfText(text, metrics, textbounds.height);
        } else {
            if ( maxnumberoflines>MAX_LINES ) {
                maxnumberoflines = MAX_LINES;
            }
            numberoflines = getMultilineOfText(text, metrics, maxnumberoflines, textbounds.height);
        }
        
        // Print the text
        int margin = (textbounds.width - numberoflines*fontheight)/2 + fontheight - metrics.getDescent()/2;
        for ( int line=0 ; line<numberoflines ; line++ ) {
            
            // Get text layout
            TextLayout textlayout = new TextLayout(lineoftext[line], font, fontrender);
            
            // Translate text layout
            AffineTransform transform = AffineTransform.getRotateInstance(Math.toRadians(-90));
            Rectangle2D rectangle = textlayout.getBounds();
            // NOTE; x and y coordinates are swapped around
            transform.translate( -textbounds.y-(textbounds.height+rectangle.getWidth())/2, textbounds.x+margin+fontheight*line );
            
            // Print text layout
            graphic.setPaint(color);
            if ( graphic.getRenderingHint(RenderingHints.KEY_ANTIALIASING)==RenderingHints.VALUE_ANTIALIAS_OFF ) {
                graphic.setRenderingHint(RenderingHints.KEY_ANTIALIASING     , RenderingHints.VALUE_ANTIALIAS_ON);
                graphic.fill( textlayout.getOutline(transform) );
                graphic.setRenderingHint(RenderingHints.KEY_ANTIALIASING     , RenderingHints.VALUE_ANTIALIAS_OFF);
            } else {
                graphic.fill( textlayout.getOutline(transform) );
            }
        }
    }
    
    static public void writeFromLeft(Graphics2D graphic, Font font, Color color, String text, Rectangle textbounds) {
        
        // Get the metrics for the current font
        FontMetrics metrics = graphic.getFontMetrics(font);
        // The ascent is the actual height of the text, but decrease it
        //  by a certain factor to make the text displayed tighter together
        int fontheight = (int) ( metrics.getAscent() * DECREASE_FONT_HEIGHT_BY_FACTOR );
        
        // Determine the number of lines that can be displayed
        int numberoflines;
        int maxnumberoflines = textbounds.width / fontheight;
        if ( maxnumberoflines<=1 ) {
            numberoflines = 1;
            getSinglelineOfText(text, metrics, textbounds.height);
        } else {
            if ( maxnumberoflines>MAX_LINES ) {
                maxnumberoflines = MAX_LINES;
            }
            numberoflines = getMultilineOfText(text, metrics, maxnumberoflines, textbounds.height);
        }
        
        // Print the text
        int margin = (textbounds.width - numberoflines*fontheight)/2 + fontheight - metrics.getDescent()/2;
        for ( int line=0 ; line<numberoflines ; line++ ) {
            
            // Get text layout
            TextLayout textlayout = new TextLayout(lineoftext[line], font, fontrender);
            
            // Translate text layout
            AffineTransform transform = AffineTransform.getRotateInstance(Math.toRadians(-90));
            Rectangle2D rectangle = textlayout.getBounds();
            // NOTE; x and y coordinates are swapped around
            transform.translate( -textbounds.y-rectangle.getWidth(), textbounds.x+margin+fontheight*line );
            
            // Print text layout
            graphic.setPaint(color);
            if ( graphic.getRenderingHint(RenderingHints.KEY_ANTIALIASING)==RenderingHints.VALUE_ANTIALIAS_OFF ) {
                graphic.setRenderingHint(RenderingHints.KEY_ANTIALIASING     , RenderingHints.VALUE_ANTIALIAS_ON);
                graphic.fill( textlayout.getOutline(transform) );
                graphic.setRenderingHint(RenderingHints.KEY_ANTIALIASING     , RenderingHints.VALUE_ANTIALIAS_OFF);
            } else {
                graphic.fill( textlayout.getOutline(transform) );
            }
        }
    }
    
    static public void writeFromRight(Graphics2D graphic, Font font, Color color, String text, Rectangle textbounds) {
        
        // Get the metrics for the current font
        FontMetrics metrics = graphic.getFontMetrics(font);
        // The ascent is the actual height of the text, but decrease it
        //  by a certain factor to make the text displayed tighter together
        int fontheight = (int) ( metrics.getAscent() * DECREASE_FONT_HEIGHT_BY_FACTOR );
        
        // Determine the number of lines that can be displayed
        int numberoflines;
        int maxnumberoflines = textbounds.width / fontheight;
        if ( maxnumberoflines<=1 ) {
            numberoflines = 1;
            getSinglelineOfText(text, metrics, textbounds.height);
        } else {
            if ( maxnumberoflines>MAX_LINES ) {
                maxnumberoflines = MAX_LINES;
            }
            numberoflines = getMultilineOfText(text, metrics, maxnumberoflines, textbounds.height);
        }
        
        // Print the text
        int margin = (textbounds.width + numberoflines*fontheight)/2 - fontheight + metrics.getDescent()/2;
        for ( int line=0 ; line<numberoflines ; line++ ) {
            
            // Get text layout
            TextLayout textlayout = new TextLayout(lineoftext[line], font, fontrender);
            
            // Translate text layout
            AffineTransform transform = AffineTransform.getRotateInstance(Math.toRadians(90));
            Rectangle2D rectangle = textlayout.getBounds();
            // NOTE; x and y coordinates are swapped around
            transform.translate( textbounds.y, -textbounds.x-margin+fontheight*line );
            
            // Print text layout
            graphic.setPaint(color);
            if ( graphic.getRenderingHint(RenderingHints.KEY_ANTIALIASING)==RenderingHints.VALUE_ANTIALIAS_OFF ) {
                graphic.setRenderingHint(RenderingHints.KEY_ANTIALIASING     , RenderingHints.VALUE_ANTIALIAS_ON);
                graphic.fill( textlayout.getOutline(transform) );
                graphic.setRenderingHint(RenderingHints.KEY_ANTIALIASING     , RenderingHints.VALUE_ANTIALIAS_OFF);
            } else {
                graphic.fill( textlayout.getOutline(transform) );
            }
        }
    }
    
    
    /* ********************************************************* */
    // Private Methods
    
    static private void getSinglelineOfText(String string, FontMetrics metrics, int maxwidth) {
        String iftoolong      = TERMINATE_IFMAXLEN_WITH;
        int    iftoolongwidth = metrics.stringWidth(iftoolong);
        printLine(0, string, metrics, 0, maxwidth, true, iftoolong, iftoolongwidth);
    }
    
    static private int getMultilineOfText(String string, FontMetrics metrics, int maxnumberoflines, int maxwidth) {
        
        int currindex = 0;
        int stringlength = string.length();
        
        boolean islastline     = false;
        String  iftoolong      = TERMINATE_HALFWORD_WITH;
        int     iftoolongwidth = metrics.stringWidth(iftoolong);
        
        // Consider all lines except what must be the last
        int currline=0;
        for (   ; currline<maxnumberoflines ; currline++ ) {
            
            // If we're on the final line, then change the iftoolong text to "..."
            if ( currline>=maxnumberoflines-1 ) {
                islastline     = true;
                iftoolong      = TERMINATE_IFMAXLEN_WITH;
                iftoolongwidth = metrics.stringWidth(iftoolong);
            }
            
            // Get the text for the next line
            currindex = printLine(currline, string, metrics, currindex, maxwidth, islastline, iftoolong, iftoolongwidth);
            if ( currindex<0 ) {
                return currline + 1;
            }
        }
        
        // If we got this far, we had to terminate before we reached the end
        //  of the string. Set the maxnumber of lines to the numberoflines
        //  variable before returning
        return maxnumberoflines;
        
    }
    
    static private int printLine(int currline, String string, FontMetrics metrics, int currindex, int maxwidth, boolean islastline, String iftoolong, int iftoolongwidth) {
        
        // Get the index of the beginning of the line
        int linestarts = currindex;
        
        // Get what can be printed on the current line
        int width = 0;
        int lastspace = -1;
        for (   ; currindex<string.length() ; currindex++ ) {
            char currchar = string.charAt(currindex);
            if ( currchar==' ' ) {
                lastspace = currindex;
            }
            width += metrics.charWidth( currchar );
            if ( width > (maxwidth-iftoolongwidth) ) {
                break;
            }
        }
        
        // Determine if we reached the end of line
        if ( currindex>=string.length() ) {
            // We can print what is left on the current line, then add
            //  it to the list and of lines and return
            lineoftext[currline] = string.substring(linestarts);
            return -1;
        } else {
            // Otherwise, we may/not not be able to depending on the length of
            //  the iftolong string; determine if it's getting in the way
            int tmpindex = currindex;
            for (   ; tmpindex<string.length() ; tmpindex++ ) {
                width += metrics.charWidth( string.charAt(tmpindex) );
                if ( width > maxwidth ) {
                    break;
                }
            }
            
            // If the iftoolong string gets in the way, then just print
            //  the current line and return, otherwise we need to continue
            if ( tmpindex>=string.length() ) {
                lineoftext[currline] = string.substring(linestarts);
                return -1;
            } else if ( lastspace>=0 && islastline==false && (currindex-lastspace)<LENGTH_TO_CUTUP_WORDS ) {
                lineoftext[currline] = string.substring(linestarts,lastspace);
                return lastspace + 1;
            } else {
                lineoftext[currline] = string.substring(linestarts,currindex) + iftoolong;
                return currindex;
            }
        }
    }
    
    
    
    /* ********************************************************* */
}
