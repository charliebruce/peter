package ember.server.util;

import java.util.Iterator;
import java.util.Set;

import ember.server.game.Entity;

public class EntityListIterator<E extends Entity> implements Iterator<E> {
    private Integer[] indicies;
    private Object[] entities;
    @SuppressWarnings("unchecked")
    private EntityList entityList;
    private int curIndex = 0;

    @SuppressWarnings("unchecked")
    public EntityListIterator(Object[] entities, Set<Integer> indicies,
                              EntityList entityList) {
        this.entities = entities;
        this.indicies = indicies.toArray(new Integer[0]);
        this.entityList = entityList;
    }

    public boolean hasNext() {
        return indicies.length != curIndex;
    }

    @SuppressWarnings("unchecked")
    public E next() {
        Object temp = entities[indicies[curIndex]];
        curIndex++;
        return (E) temp;
    }

    public void remove() {
        if (curIndex >= 1) {
            entityList.remove(indicies[curIndex - 1]);
        }
    }
}
