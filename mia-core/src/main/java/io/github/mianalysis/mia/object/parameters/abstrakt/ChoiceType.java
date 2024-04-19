package io.github.mianalysis.mia.object.parameters.abstrakt;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import io.github.mianalysis.mia.MIA;
import io.github.mianalysis.mia.module.Module;
import io.github.mianalysis.mia.module.system.GlobalVariables;
import io.github.mianalysis.mia.object.Workspace;
import io.github.mianalysis.mia.process.ParameterControlFactory;

public abstract class ChoiceType extends Parameter {
    protected String choice = "";
    protected boolean showChoice = true; // When true GUI shows a choice drop-down, when false GUI shows a text entry
                                         // box

    public ChoiceType(String name, Module module) {
        super(name, module);
    }

    public ChoiceType(String name, Module module, String description) {
        super(name, module, description);
    }

    public String getChoice() {
        return choice;

    }

    public void setChoice(String choice) {
        if (choice == null)
            choice = "";
        this.choice = choice;
    }

    public abstract String[] getChoices();

    public void setShowChoice(boolean showChoice) {
        this.showChoice = showChoice;
    }

    public boolean isShowChoice() {
        return this.showChoice;
    }

    @Override
    public String getRawStringValue() {
        return choice;
    }

    @Override
    public void setValueFromString(String string) {
        if (string == null)
            string = "";

        this.choice = string;

    }

    @Override
    protected ParameterControl initialiseControl() {
        return ParameterControlFactory.getChoiceTypeControl(this);
    }

    @Override
    public <T> T getValue(Workspace workspace) {
        String converted = GlobalVariables.convertString(choice, module.getModules());
        converted = TextType.insertWorkspaceValues(converted, workspace);
        converted = TextType.applyCalculation(converted);

        return (T) converted;

    }

    @Override
    public <T> void setValue(T value) {
        choice = (String) value;
    }

    @Override
    public void appendXMLAttributes(Element element) {
        super.appendXMLAttributes(element);

        element.setAttribute("SHOW_CHOICE", Boolean.toString(isShowChoice()));

    }

    @Override
    public void setAttributesFromXML(Node node) {
        super.setAttributesFromXML(node);

        NamedNodeMap map = node.getAttributes();

        // ChoiceType values can be from hard-coded sources, so we check if they're
        // labelled for reassignment.
        String xmlValue = map.getNamedItem("VALUE").getNodeValue();
        xmlValue = MIA.getLostAndFound().findParameterValue(module.getClass().getSimpleName(), getName(), xmlValue);
        setValueFromString(xmlValue);

        setVisible(Boolean.parseBoolean(map.getNamedItem("VISIBLE").getNodeValue()));

        if (map.getNamedItem("SHOW_CHOICE") != null)
            setShowChoice(Boolean.parseBoolean(map.getNamedItem("SHOW_CHOICE").getNodeValue()));

    }

    @Override
    public boolean verify() {
        if (isShowChoice()) {
            // Verifying the choice is present in the choices. When we generateModuleList
            // getChoices, we should be getting the valid
            // options only.
            String[] choices = getChoices();

            for (String currChoice : choices)
                if (choice.equals(currChoice))
                    return true;

            return false;
        } else {
            return GlobalVariables.variablesPresent(getRawStringValue(), module.getModules());
        }
    }
}
