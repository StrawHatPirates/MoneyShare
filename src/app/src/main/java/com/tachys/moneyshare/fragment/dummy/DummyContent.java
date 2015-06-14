package com.tachys.moneyshare.fragment.dummy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DummyContent {

    public static List<DummyItem> ITEMS = new ArrayList<>();

    public static Map<String, DummyItem> ITEM_MAP = new HashMap<>();

    static {
        addItem(new DummyItem("150", "Item 1"));
        addItem(new DummyItem("-40", "Item 2"));
        addItem(new DummyItem("220", "Item 3"));
    }

    private static void addItem(DummyItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.amt, item);
    }

    public static class DummyItem {
        public String amt;
        public String title;

        public DummyItem(String id, String content) {
            this.amt = id;
            this.title = content;
        }

        @Override
        public String toString() {
            return title;
        }
    }
}
