package org.example.bazaartracker;

import org.json.simple.parser.JSONParser;

import java.awt.*;

public class Instance {
    public static final JSONParser parser = new JSONParser();
    public static final Color TRADER_BACKGROUND_DARK = new Color(0x0D1B2A);
    public static final Color TRADER_BACKGROUND_SECTION = new Color(0x102030);
    public static final Color TRADER_TEXT_PRIMARY = new Color(0xD0E8F0);
    public static final Color TRADER_TEXT_SECONDARY = new Color(0xA0BCC8);
    public static final Color TRADER_ACCENT_TEAL = new Color(0x33FFC7);
    public static final Color TRADER_GRIDLINE_BORDER = new Color(0x2A4A68);
    public static final Color TRADER_ACCENT_SECONDARY_LINE = new Color(0x50A0B8);
    public static final Font CHART_TITLE_FONT = new Font("Roboto", Font.BOLD, 16);
    public static final Font AXIS_LABEL_FONT = new Font("Roboto", Font.PLAIN, 12);
    public static final Font AXIS_TICK_LABEL_FONT = new Font("Roboto", Font.PLAIN, 10);

}
