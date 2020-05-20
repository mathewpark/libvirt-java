package org.libvirt;

import java.util.Arrays;

import org.libvirt.jna.Libvirt;
import org.libvirt.jna.virSchedParameter;
import org.libvirt.jna.virSchedParameterValue;

import com.sun.jna.Native;
import java.io.UnsupportedEncodingException;

/**
 * The abstract parent of the actual Schedparameter classes
 *
 * @author stoty
 *
 */
public abstract class SchedParameter {

    public static SchedParameter create(virSchedParameter vParam) {
        SchedParameter returnValue = null;
        if (vParam != null) {
            switch (vParam.type) {
                case (1):
                    returnValue = new SchedIntParameter(vParam.value.i);
                    break;
                case (2):
                    returnValue = new SchedUintParameter(vParam.value.i);
                    break;
                case (3):
                    returnValue = new SchedLongParameter(vParam.value.l);
                    break;
                case (4):
                    returnValue = new SchedUlongParameter(vParam.value.l);
                    break;
                case (5):
                    returnValue = new SchedDoubleParameter(vParam.value.d);
                    break;
                case (6):
                    returnValue = new SchedBooleanParameter(vParam.value.b);
                    break;
                default:
                    // Unknown type: nothing to do.
            }
            if (returnValue != null) {
                returnValue.field = Native.toString(vParam.field);
            }
        }
        return returnValue;
    }

    public static virSchedParameter toNative(SchedParameter param) {
        virSchedParameter returnValue = new virSchedParameter();
        returnValue.value = new virSchedParameterValue();
        try {
            returnValue.field = copyOf(param.field.getBytes("UTF-8"), Libvirt.VIR_DOMAIN_SCHED_FIELD_LENGTH);
        } catch (UnsupportedEncodingException ex) {
            returnValue.field = null;
        }
        returnValue.type = param.getType();
        switch (param.getType()) {
            case (1):
                returnValue.value.i = ((SchedIntParameter) param).value;
                returnValue.value.setType(int.class);
                break;
            case (2):
                returnValue.value.i = ((SchedUintParameter) param).value;
                returnValue.value.setType(int.class);
                break;
            case (3):
                returnValue.value.l = ((SchedLongParameter) param).value;
                returnValue.value.setType(long.class);
                break;
            case (4):
                returnValue.value.l = ((SchedUlongParameter) param).value;
                returnValue.value.setType(long.class);
                break;
            case (5):
                returnValue.value.d = ((SchedDoubleParameter) param).value;
                returnValue.value.setType(double.class);
                break;
            case (6):
                returnValue.value.b = (byte) (((SchedBooleanParameter) param).value ? 1 : 0);
                returnValue.value.setType(byte.class);
                break;
            default:
                // Unknown type: nothing to do.
        }
        return returnValue;
    }

    public static byte[] copyOf(byte[] original, int length) {
        byte[] returnValue = new byte[length];
        int originalLength = original.length;
        Arrays.fill(returnValue, (byte) 0);
        System.arraycopy(original, 0, returnValue, 0, originalLength);
        return returnValue;
    }

    /**
     * Parameter name
     */
    public String field;

    /**
     * The type of the parameter
     *
     * @return the Type of the parameter
     */
    public abstract int getType();

    /**
     * Utility function for displaying the type
     *
     * @return the Type of the parameter as string
     */
    public abstract String getTypeAsString();

    /**
     * Utility function for displaying the value
     *
     * @return the value of the parameter in String form
     */
    public abstract String getValueAsString();
}
