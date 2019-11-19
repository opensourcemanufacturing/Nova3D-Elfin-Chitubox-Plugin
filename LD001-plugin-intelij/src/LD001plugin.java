import com.sun.javafx.binding.StringFormatter;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

public class LD001plugin {
  public static void main(String... args) {
    try {
      System.out.println("Out to:" + args[0] + "slice.gcode\n");
      OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(args[0] + "slice.gcode"));
      String template = ";all variables are accessible by Apache Freemaker SQUARE_BRACKET_INTERPOLATION_SYNTAX\n";
      template += ";you can review final template with ;printFreemakerTemplate=true comment\n";
      BufferedReader isr = null;
      try {
        isr = new BufferedReader(new FileReader(args[0] + "run.gcode"));
        Map<String, Object> data = new HashMap<>();
        boolean header = true;
        boolean startCode = false;
        boolean printCode = false;
        boolean endCode = false;
        boolean printData = false;
        boolean freemakerTemplate = false;
        long bottomLayers = 0;
        List<Map<String, Object>> slices = new ArrayList<>();
        data.put("slices", slices);
        HashMap<String, Object> slice = null;
        for (String line = isr.readLine(); line != null; line = isr.readLine()) {
          if (line.startsWith(";printFreemakerTemplate=true"))
            freemakerTemplate = true;
          else if (line.startsWith(";START_GCODE_BEGIN")) {
            header = false;
            startCode = true;
            template += ";Number of Slices = "+data.get("totalLayer")+"\n";
            template += ";Z Lift Feed Rate = " + data.get("normalLayerLiftSpeed") + "\n";
            template += ";Lift Distance = " + data.get("normalLayerLiftHeight") + "\n";
            template += ";ROW CALCULATED VALUES\n";
            template += ";image: zero based image/slice index\n";
            template += ";exposure_time: calculated exposure time for image/slice/layer\n";
            template += ";rise_dist: calculated rise distance for image/slice/layer (relative)\n";
            template += ";fall_dist: calculated fall distance for image/slice/layer (relative)\n";
            template += ";rise_pos: calculated rise position for image/slice/layer (absolute)\n";
            template += ";fall_pos: calculated fall distance for image/slice/layer (absolute)\n";
            template += ";rise_speed: calculated rise speed for image/slice/layer\n";
            template += ";fall_speed: calculated fall speed for image/slice/layer\n";
            template += ";machine_height: copy of 'machineZ'\n";
            template += "<#assign machine_height=machineZ>\n";
            template += line + "\n";
          } else if (line.startsWith(";START_GCODE_END")) {
            startCode = false;
            template += line + "\n";
          } else if (startCode || endCode)
            template += line + "\n";
          else if (line.startsWith(";LAYER_START:")) {
            if (line.startsWith(";LAYER_START:0")) {
              printCode = true;
              template += "<#list slices as slice>\n";
              template += "<#assign image=slice.image exposure_time=slice.exposure_time rise_dist=slice.rise_dist fall_dist=slice.fall_dist>\n";
              template += "<#assign rise_pos=slice.rise_pos fall_pos=slice.fall_pos rise_speed=slice.rise_speed fall_speed=slice.fall_speed>\n";
              template += "<#assign layer=slice.layer fall_pos=slice.fall_pos rise_speed=slice.rise_speed fall_speed=slice.fall_speed>\n";
              template += "\n;LAYER_START:[=layer]\n";
              bottomLayers = (long) data.get("bottomLayerCount");
            }
            printData = true;
            slice = new HashMap<>();
            slices.add(slice);
            long layer = Long.parseLong(line.substring(line.lastIndexOf(":") + 1));
            slice.put("image", layer + ".png");
            slice.put("layer", layer);
            slice.put("exposure_time", (layer < bottomLayers) ? data.get("bottomLayerExposureTime") : data.get("normalExposureTime"));
            slice.put("rise_dist", (layer < bottomLayers) ? data.get("bottomLayerLiftHeight") : data.get("normalLayerLiftHeight"));
            slice.put("fall_dist", (layer < bottomLayers)
              ? (long) data.get("bottomLayerLiftHeight") - (double) data.get("layerHeight")
              : (long) data.get("normalLayerLiftHeight") - (double) data.get("layerHeight"));
            slice.put("rise_pos", (layer < bottomLayers)
              ? (long) data.get("bottomLayerLiftHeight") + layer * (double) data.get("layerHeight")
              : (long) data.get("normalLayerLiftHeight") + layer * (double) data.get("layerHeight"));
            slice.put("fall_pos", (layer + 1) * (double) data.get("layerHeight"));
            slice.put("rise_speed", (layer < bottomLayers) ? data.get("bottomLayerLiftSpeed") : data.get("normalLayerLiftSpeed"));
            slice.put("fall_speed", data.get("normalDropSpeed"));
          } else if (line.startsWith(";END_GCODE_BEGIN")) {
            template += "\n" + line + "\n";
            endCode = true;
          } else if (printData) {
            if (printCode) {
              if (line.startsWith(";currPos:")) {
                template += ";currPos:[=layer*layerHeight]\n";
              } else {
                template += line + "\n";
                if (line.startsWith(";LAYER_END")) {
                  printCode = false;
                  template += "</#list>\n";
                }
              }
            }
          } else if (header) {
            if (line.startsWith(";")) {
              template += line + "\n";
              String[] pair = line.split(":", 2);
              try {
                data.put(pair[0].substring(1), Long.parseLong(pair[1]));
              } catch (NumberFormatException e) {
                try {
                  data.put(pair[0].substring(1), Double.parseDouble(pair[1]));
                } catch (NumberFormatException e1) {
                  data.put(pair[0].substring(1), pair[1]);
                }
              }
            }
          }
        }
        isr.close();
        isr = null;
        if (freemakerTemplate)
          throw new InterruptedException("Interupt for freemaker template print");
        Configuration cfg = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        cfg.setDirectoryForTemplateLoading(new File(args[0]));
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        cfg.setLogTemplateExceptions(false);
        cfg.setWrapUncheckedExceptions(false);
        cfg.setFallbackOnNullLoopVariable(false);
        cfg.setInterpolationSyntax(Configuration.SQUARE_BRACKET_INTERPOLATION_SYNTAX);
        cfg.setSetting("number_format", "computer");
        new Template("slicer", new StringReader(template), cfg).process(data, osw);
        osw.flush();
        for (int i = 0; i < (long) data.get("totalLayer"); i++) {
          Path orig = Paths.get(args[0] + (i + 1) + ".png");
          Files.move(orig, orig.resolveSibling(String.format("slice%05d.png", i)));
        }
        Path file = Paths.get(args[0] + "run.gcode");
        Files.delete(file);
        Path sourceFile = Paths.get(args[0] + "../plugin/LD001/nova3d-reference_slice.conf");
        file = Paths.get(args[0] + "slice.conf");
        Files.copy(sourceFile, file, StandardCopyOption.REPLACE_EXISTING );
        file = Paths.get(args[0] + "preview.png");
        Files.move(file, file.resolveSibling("preview_cropping.png"), StandardCopyOption.REPLACE_EXISTING);
        osw.close();
      } catch (Throwable e) {
        PrintWriter printWriter = new PrintWriter(osw);
        e.printStackTrace(printWriter);
        int lineNumber = 1;
        for (String line : template.split("\n"))
          printWriter.write(String.format("/* %3d */ %s%s", lineNumber++, line, "\n"));
        printWriter.flush();
        printWriter.close();
        if (isr != null) isr.close();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  ;
}
