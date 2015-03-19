/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.solidleon.alpha.cursormode;

import com.solidleon.alpha.Font;
import com.solidleon.alpha.InGameState;
import com.solidleon.alpha.TileSet;
import com.solidleon.alpha.game.world.Chunk;
import com.solidleon.alpha.helpsystem.HelpSystem;
import com.solidleon.alpha.helpsystem.HelpTopic;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.state.StateBasedGame;

/**
 *
 * @author Markus
 */
public class IngameHelpCursor extends CursorMode {

    private HelpSystem help;
    private HelpTopic topic;
    private int scroll;
    private StringBuilder search = new StringBuilder();
    private List<HelpTopic> history = new ArrayList<HelpTopic>();
    private int historyPointer = -1;
    private int errorTimerTopicNotFound;
    private boolean onChangeTimerEnabled = true;
    private int onChangeTimer;
    private int onChangeColor;

    public IngameHelpCursor() {
        help = HelpSystem.getInstance();
        try {
            help.load();
            changeTopic(help.getMatchingTopic("index"));
        } catch (FileNotFoundException ex) {
        } catch (IOException ex) {
        }
    }

    @Override
    public void renderCustom(GameContainer container, StateBasedGame game, Graphics g, InGameState aThis) {
        // Inner width (without borders)
        int w = container.getWidth() - 16 - 32;
        int h = container.getHeight() - 24 - 32;

        int ox = 16;
        int oy = 16;

        g.setColor(Color.black);
        g.fillRect(0, 0, container.getWidth(), container.getHeight());
        TileSet.drawFrame(0, 0, container.getWidth() - 16, container.getHeight() - 24);

        if (topic != null) {
            int maxLines = h / Font.getLineHeight();
            int c = 0;
            for (int i = scroll; i < topic.content.size(); i++) {
                if (c >= maxLines) {
                    break;
                }
                String line = topic.content.get(i);
                Font.drawString(line, ox, oy + c * Font.getLineHeight(), Color.white);
                c++;
            }

            int x = w - Font.getWidth(topic.topicName);
            int y = h + 16;
            TileSet.drawBackground(x - 2, y, Font.getWidth(topic.topicName) + 4, Font.getLineHeight(), Color.black);
            Font.drawString(topic.topicName, x, y,
                    onChangeColor % 2 == 0 ? Color.white : Color.cyan);

        } else {
            Font.drawString("NO HELP AVAILABLE",
                    w / 2 - Font.getWidth("NO HELP AVAILABLE") / 2,
                    h / 2 - Font.getLineHeight() / 2,
                    Color.white);
        }
        if (search != null && search.length() > 0) {
            g.setColor(Color.black);
            g.fillRect(ox, h + 16, Font.getWidth("Search: " + search.toString()) + 4, Font.getLineHeight());

            Font.drawString("Search: ",
                    ox + 2,
                    h + 16,
                    Color.white);
            Font.drawString(search.toString(),
                    ox + 2 + Font.getWidth("Search: "),
                    h + 16,
                    Color.cyan);

        }

        if (errorTimerTopicNotFound > 0) {
            String s = "Topic Not Found";
            int boxx = w / 2 - Font.getWidth(s) / 2 - 16;
            int boxy = h / 2 - Font.getLineHeight() / 2 - 16;
            int boxw = Font.getWidth(s) + 32;
            int boxh = Font.getLineHeight() + 32;

            TileSet.drawFrame(boxx, boxy, boxw, boxh, Color.white, Color.red);
            Font.drawString(s, boxx + 16, boxy + 16, Color.white);
        }
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta, InGameState ingame) {
        if (errorTimerTopicNotFound > 0) {
            errorTimerTopicNotFound -= delta;
        }
        if (onChangeTimerEnabled) {

            if (onChangeTimer <= 0) {
                onChangeColor++;
                if (onChangeColor == 12) {
                    onChangeTimerEnabled = false;
                } else {
                    onChangeTimer = 150;
                }
            } else {
                onChangeTimer -= delta;
            }
        }
    }

    @Override
    public void keyPressed(InGameState game, Chunk area, int xCursor, int yCursor, int key, char c) {
        if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == ':' || c == ' ') {
            search.append(c);
        }
        if (key == Input.KEY_BACK) {
            if (search.length() > 0) {
                search.setLength(search.length() - 1);
            }
        }
        if (key == Input.KEY_ENTER) {
            submit(game);
        }
        if (key == Input.KEY_ESCAPE) {
            game.changeMode(oldCursorMode);
        }
        if (topic != null) {
            if (key == Input.KEY_UP) {
                scroll--;
            }
            if (key == Input.KEY_DOWN) {
                scroll++;
            }
            int len = topic.content.size();
            if (scroll < 0) {
                scroll += len;
            }
            if (scroll >= len) {
                scroll -= len;
            }
        }
    }

    private void submit(InGameState game) {
        if (search.length() <= 0) {
            return;
        }
        if (search.charAt(0) == ':') {
            String s = search.toString();
            if (":reload".equals(s)) {
                //TODO: Fix this up
                try {
                    help.load();
                    topic = help.getMatchingTopic(topic.topicName);
                } catch (FileNotFoundException ex) {
                } catch (IOException ex) {
                }
            } else if (":close".equals(s)) {
                if (oldCursorMode instanceof IngameHelpCursor) {
                    game.changeMode(new LookMode());
                } else {
                    game.changeMode(oldCursorMode);
                }
            } else if (":options".equals(s)) {
                game.changeMode(new OptionsMode(this));
            } else if (":save".equals(s)) {
                game.save();
            } else if (":load".equals(s)) {
                game.load();
            } else if (":reset".equals(s)) {
                game.resetGame();
            } else if (":quit".equals(s)) {
                game.stop();
            } else if (":back".equals(s)) {
                historyBack(1);
            }
        } else {
            HelpTopic result = help.getMatchingTopic(search.toString());
            if (result == null) {
                errorTimerTopicNotFound = 1000;
            } else {
                changeTopic(result);
            }
        }
        search.setLength(0);
    }

    private void changeTopic(HelpTopic newTopic) {
        topic = newTopic;
        scroll = 0;
        onChangeTimerEnabled = true;
        onChangeColor = 0;
        history.add(topic);
        historyPointer = history.size() - 1;
    }

    private void historyBack(int n) {
        if (n == 0) {
            return;
        }
        if (n < 0) {
            throw new IllegalArgumentException("HistoryBack takes only positive values!");
        }
        int idx = historyPointer - n;
        if (idx < 0) {
            idx = 0;
        }
        if (idx >= history.size()) {
            idx = history.size() - 1;
        }
        if (idx < 0) {
            return;
        }
        topic = history.get(idx);
        historyPointer = idx;

    }
}
