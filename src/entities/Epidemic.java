package entities;

import common.Globals;
import common.Infection;
import pt.ua.gboard.GBoard;
import region.laboratory.BacteriaLaboratory;
import region.worldregion.EarthRegion;
import region.worldregion.EarthZone;
import region.worldregion.Location;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by espinha on 1/25/17.
 */
public class Epidemic extends BiologicalEntity implements Runnable {

    private final BacteriaLaboratory laboratory;
    private Vector<Bacteria> bacterias;
    private Set<Location> locations;

    public Epidemic(GBoard board, EarthRegion region, BacteriaLaboratory laboratory, Location location) {
        super(board, region);

        this.laboratory = laboratory;

        this.locations = new LinkedHashSet<>();
        locations.add(location);

        System.out.println("Started thread: " + Thread.currentThread().getId());
    }


    @Override
    public void run() {

        boolean running = true;


        while(running) {

            laboratory.develop(this);

            int size = bacterias.size();

            region.spread(this, locations);

            //System.out.println("Epidemic borders: " + locations.size());
            //System.out.println("Epidemic bacterias:" + bacterias.size());

            if(size > bacterias.size())
                System.out.println("There's only a matter of me, in a matter of speaking I'm dead");

            running = bacterias.size() > 0;

            Globals.metronome().sync();
        }
        /**
         * Lifecyle:
         * createBacterias()
         * while(conditionToRun) {
         *      area.infect(targetArea);
         *      laboratory.reproduceBacterias(bacterias)
         *
         *      conditionToRun = infectedPopulation > 0 && remaining life > 0
         *      Globals.tick();
         * }
         *
         */

        /*

        while(alive && lifespan > 0) {
            MutableCountry country = Globals.randomCountry(continent);  // TODO: change to random area
            Globals.randomPause(500,2000);
            alive = continent.spreadInfection(this, country.getCODE());

            int development = laboratory.develop(this);
            infection.updateSeverity(development);

            for(Bacteria bacteria: bacterias) {

                Infection infection = bacteria.newInfection();
                //System.out.println(Thread.currentThread().getId() + " contagion: " + infection.getContagion());






                int contagion = infection.getContagion();

                int increase = new Random(productionTime).nextInt(contagion);
                infection.increaseContagion(increase);

                if(increase + 1 > contagion/2) { // greater than half
                    continent.spreadInfectionToBorders(this, country.getCODE());
                }

                //System.out.println("ran all borders");
                productionTime++;
            }
        }*/
    }

    public Vector<Bacteria> bacterias() {
        return bacterias;
    }

    public void setBacterias(Vector<Bacteria> bacterias) {
        if(this.bacterias == null)
            this.bacterias = bacterias;
        else
            Collections.copy(bacterias, this.bacterias);
    }

    public void expand(List<Location> borders) {
        locations.addAll(borders);
    }
}
