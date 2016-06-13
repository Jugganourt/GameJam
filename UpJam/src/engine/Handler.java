package engine;

import engine.entities.Entities;
import engine.entities.OpacityLayer;
import engine.entities.Player;
import gui.Game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.LinkedList;

import javax.sound.sampled.Clip;

import map.Camera;
import map.Map;
import map.Tile;


public class Handler {
	
	private Game game;
	private Map map;
	private Player player;
	private LinkedList<Entities> robots = new LinkedList<Entities>();
	private LinkedList<Entities> extras = new LinkedList<Entities>();
	private Camera cam;
	private OpacityLayer[][] fog;
	private Clip clip;
	public Handler(Game game) {
		this.game = game;
	}
	
	public void createMap(Map map){
		this.map = map;
		fog = new OpacityLayer[map.getWidth()][map.getHeight()];
		for(int y = 0; y<map.getHeight();y++){
			for(int x = 0; x<map.getWidth();x++){
				fog[x][y] = new OpacityLayer(x*Tile.TILESIZE , y*Tile.TILESIZE , Tile.TILESIZE);
			}
		}
		
	}
	
	public void addPlayer(Player player){
		this.player = player;
		player.addMap(map);
	}

	public void addRobot(Entities Robot){
		robots.add(Robot);
	}
	
	public void addExtras(Entities trap) {
		extras.add(trap);		
	}

	public void tick(){
		//No tick needed for Map
		
		//Tick Objects - May not be needed
		for (int i = 0; i < extras.size(); i++) {
			extras.get(i).tick();
		}
		player.tick();
		cam.tick(player);
		
		if(!Game.isDay){
			for (int i = 0; i < robots.size(); i++) {
				robots.get(i).tick();
			}
			for(OpacityLayer[] p: fog)
				for(OpacityLayer q: p)
					q.setOpacity(0);
			
			int x = (player.getXCoord()+(Tile.TILESIZE/2))/Tile.TILESIZE;
			int y = (player.getYCoord()+(Tile.TILESIZE/2))/Tile.TILESIZE;
			fog[x][y].setOpacity(1);
			try{
			fog[x-1][y-1].spreadOpacityCorner(x, y, 1, fog, map);
			fog[x-1][y+1].spreadOpacityCorner(x, y, 1, fog, map);
			fog[x+1][y+1].spreadOpacityCorner(x, y, 1, fog, map);
			fog[x+1][y-1].spreadOpacityCorner(x, y, 1, fog, map);
			fog[x-1][y].spreadOpacityCentre(x, y, 1, fog, map);
			fog[x][y+1].spreadOpacityCentre(x, y, 1, fog, map);
			fog[x][y-1].spreadOpacityCentre(x, y, 1, fog, map);
			fog[x+1][y].spreadOpacityCentre(x, y, 1, fog, map);
			}catch(ArrayIndexOutOfBoundsException e){}
//			
//			fog[(game.WIDTH/Tile.TILESIZE)][(game.HEIGHT/Tile.TILESIZE)].setOpacity(1);
//			fog[(game.WIDTH/Tile.TILESIZE)][(game.HEIGHT/Tile.TILESIZE)].spreadOpacityCorner(((game.WIDTH/Tile.TILESIZE) )+1, ((game.HEIGHT/Tile.TILESIZE) )+1, 1, fog, map);
//			
//			fog[(game.WIDTH/Tile.TILESIZE) +1][(game.HEIGHT/Tile.TILESIZE) ].setOpacity(1);
//			fog[(game.WIDTH/Tile.TILESIZE) +1][(game.HEIGHT/Tile.TILESIZE) ].spreadOpacityCorner(((game.WIDTH/Tile.TILESIZE) ), ((game.HEIGHT/Tile.TILESIZE) )+1, 1, fog, map);
//			
//			fog[(game.WIDTH/Tile.TILESIZE) ][(game.HEIGHT/Tile.TILESIZE) +1].setOpacity(1);
//			fog[(game.WIDTH/Tile.TILESIZE) ][(game.HEIGHT/Tile.TILESIZE) +1].spreadOpacityCorner(((game.WIDTH/Tile.TILESIZE) )+1, ((game.HEIGHT/Tile.TILESIZE) ), 1, fog, map);
//			
//			fog[(game.WIDTH/Tile.TILESIZE) +1][(game.HEIGHT/Tile.TILESIZE) +1].setOpacity(1);
//			fog[(game.WIDTH/Tile.TILESIZE) +1][(game.HEIGHT/Tile.TILESIZE) +1].spreadOpacityCorner((game.WIDTH/Tile.TILESIZE) , (game.HEIGHT/Tile.TILESIZE) , 1, fog, map);
			
		}
	}
	
	
	public void render(Graphics g){
		//Render the map
		for (int y = 0; y < map.getHeight(); y++) {
			for (int x = 0; x < map.getWidth(); x++) {
				if (map.getTile(x, y).getXCoord() >= (0 - cam.getX() - Tile.TILESIZE)
						&& map.getTile(x, y).getYCoord() >= (0 - cam.getY() - Tile.TILESIZE)
						&& map.getTile(x, y).getXCoord() <= (game.getWidth() - cam.getX())
						&& map.getTile(x, y).getYCoord() <= (game.getHeight() - cam.getY())) {
					map.getTile(x, y).render(g);
					//System.out.println(((1-map.getTile(x, y).getOpacity())*255));
//					g.setColor(new Color(0, 0, 0, (int)((1-map.getTile(x, y).getOpacity())*255)));
//					g.fillRect(x*Tile.TILESIZE, y*Tile.TILESIZE, Tile.TILESIZE, Tile.TILESIZE);
					map.getTile(x, y).setOpacity(0.05);
				}
			}
		}
		
		//Render Extras behind players 
		for (int i = 0; i < extras.size(); i++) {
			extras.get(i).render(g);
		}
		
	
		
		//Render players and AI
		player.render(g);
			
		//Render AI
		if(!Game.isDay){
			for (int i = 0; i < robots.size(); i++) {
				robots.get(i).render(g);
			}
			for(OpacityLayer[] p: fog){
				for(OpacityLayer q: p)
					q.render(g);
			}
		}
		
		//Render objects ontop
		
	}

	public LinkedList<Entities> getExtras() {
		return this.extras;
	}

	public void addCamera(Camera cam) {
		this.cam = cam;		
	}


	
	
}
