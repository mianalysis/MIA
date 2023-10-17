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

package signalprocesser.voronoi.statusstructure;

import java.util.ArrayList;

import signalprocesser.voronoi.VPoint;
import signalprocesser.voronoi.VoronoiShared;
import signalprocesser.voronoi.eventqueue.EventQueue;
import signalprocesser.voronoi.eventqueue.VCircleEvent;
import signalprocesser.voronoi.eventqueue.VSiteEvent;

public class VLinkedNode {
    
    /* ***************************************************** */
    // Variables
    
    public VSiteEvent siteevent;
    
    private VLinkedNode prev;
    private VLinkedNode next;
    
    private ArrayList<VCircleEvent> circleevents;
    
    /* ***************************************************** */
    // Constructors
    
    protected VLinkedNode() { }
    public VLinkedNode(VSiteEvent _siteevent) {
        this.siteevent = _siteevent;
    }
    
    /* ***************************************************** */
    // Methods
    
    public boolean hasCircleEvents() {
        return ( circleevents!=null && circleevents.size()>0 );
    }
    public ArrayList<VCircleEvent> getCircleEvents() { return circleevents; }
    public void removeCircleEvents(EventQueue eventqueue) {
        if ( circleevents==null ) return;
        
        for ( VCircleEvent circleevent : circleevents ) {
            // Unlink to leaf node
            circleevent.leafnode = null;
            
            // Remove from queue
            boolean flag = eventqueue.removeEvent( circleevent );
            // IGNORE FLAG - errors being thrown as a result of using
            //  this flag from circle events when this method is called...
            //if ( flag==false ) {
            //    throw new RuntimeException("Event not removed from queue when expected to be able to.");
            //}
        }
        circleevents = null;
    }
    
    public void addCircleEvent(EventQueue eventqueue) {
        if ( prev!=null && next!=null ) {
            VCircleEvent circleevent = VoronoiShared.calculateCenter(prev.siteevent, this.siteevent, next.siteevent);
            if ( circleevent!=null ) {
                addCircleEvent( circleevent );
                circleevent.leafnode = this;
                eventqueue.addEvent(circleevent);
            }
        }
    }
    private void addCircleEvent(VCircleEvent _circleevent) {
        if ( circleevents==null ) {
            circleevents = new ArrayList<VCircleEvent>();
        }
        circleevents.add( _circleevent );
    }
    
    public VLinkedNode getPrev() { return prev; }
    public VLinkedNode getNext() { return next; }
    
    public void setNext(VLinkedNode node) {
        // Set forward link
        if ( next!=null ) {
            next.prev = null;
        }
        this.next = node;
        
        // Set back link
        if ( node!=null ) {
            if ( node.prev!=null ) {
                node.prev.next = null;
            }
            
            node.prev = this;
        }
    }
    
    public VPoint getIntersectWithNext(int sweepline) {
        VSiteEvent v1 = siteevent;
        VSiteEvent v2 = next.siteevent;
        
        // Calculate a, b and c of the parabola
        v1.calcParabolaConstants(sweepline);
        v2.calcParabolaConstants(sweepline);
        
        // Determine where two parabola meet
        double intersects[] = VoronoiShared.solveQuadratic(v2.a-v1.a, v2.b-v1.b, v2.c-v1.c);
        return new VPoint( (int) intersects[0] , sweepline + siteevent.getYValueOfParabola(intersects[0]) );
    }
    
    public boolean isLeafNode() { return true; }
    
    public boolean isInternalNode() { return false; }
    
    public VLinkedNode cloneLinkedNode() {
        VLinkedNode clone = new VLinkedNode(this.siteevent);
        // DO NOT DUPLICATE prev/next values
        // DO NOT DUPLICATE circle events???? (?!)
        return clone;
    }
    
    /* ***************************************************** */
}
