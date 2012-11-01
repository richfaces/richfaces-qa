package org.richfaces.tests.metamer.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import org.richfaces.convert.SequenceRowKeyConverter;
import org.richfaces.tests.metamer.model.CompactDisc;
import org.richfaces.tests.metamer.model.Company;
import org.richfaces.tests.metamer.model.Labeled;
import org.richfaces.tests.metamer.model.State;
import org.richfaces.tests.metamer.model.tree.TreeNodeWithContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@FacesConverter("treeRowKeyConverter")
public class TreeRowKeyConverter extends SequenceRowKeyConverter {

    private static final Logger LOGGER = LoggerFactory.getLogger(TreeRowKeyConverter.class);

    public TreeRowKeyConverter() {
        super(TreeNodeWithContent.class, new Delegated());
    }

    private static class Delegated implements Converter {

        @Override
        public Object getAsObject(FacesContext context, UIComponent component, String value) {
            LOGGER.info("converting <" + value + "> to object");
            if (value.startsWith("[converted] company: ")) {
                return new Company(value.replace("[converted] company: ", ""));
            } else if (value.startsWith("[converted] state: ")) {
                State state = new State();
                state.setName(value.replace("[converted] state: ", ""));
                return state;
            } else if (value.startsWith("[converted] cd: ")) {
                String[] cd = value.replace("[converted] cd: ", "").split(" - ");
                return new CompactDisc(cd[1], cd[0]);
            } else {
                return Integer.parseInt(value);
            }
        }

        @Override
        public String getAsString(FacesContext context, UIComponent component, Object value) {
            LOGGER.info("converting <" + value + "> to String");
            if (value instanceof Integer) {
                return value.toString();
            } else if (value instanceof Company) {
                return "[converted] company: " + ((Labeled) value).getLabel();
            } else if (value instanceof State) {
                return "[converted] state: " + ((Labeled) value).getLabel();
            } else if (value instanceof CompactDisc) {
                return "[converted] cd: " + ((Labeled) value).getLabel();
            } else {
                throw new UnsupportedOperationException("Can't convert " + value + " of type " + value.getClass().getName());
            }
        }
    }
}
