package com.google.refine.commands.project;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.refine.ProjectManager;
import com.google.refine.browsing.Engine;
import com.google.refine.commands.Command;
import com.google.refine.exporters.CsvExporter;
import com.google.refine.exporters.Exporter;
import com.google.refine.exporters.ExporterRegistry;
import com.google.refine.exporters.StreamExporter;
import com.google.refine.exporters.WriterExporter;
import com.google.refine.model.Project;

public class ExportRowsCommand extends Command {

    @SuppressWarnings("unchecked")
	static public Properties getRequestParameters(HttpServletRequest request) {
        Properties options = new Properties();
        
        Enumeration<String> en = request.getParameterNames();
        while (en.hasMoreElements()) {
        	String name = en.nextElement();
        	options.put(name, request.getParameter(name));
        }
    	return options;
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ProjectManager.singleton.setBusy(true);
        try {
            Project project = getProject(request);
            Engine engine = getEngine(request, project);
            String format = request.getParameter("format");
            Properties options = getRequestParameters(request);

            Exporter exporter = ExporterRegistry.getExporter(format);
            if (exporter == null) {
                exporter = new CsvExporter('\t');
            }

            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type", exporter.getContentType());

            if (exporter instanceof WriterExporter) {
                PrintWriter writer = response.getWriter();
                ((WriterExporter) exporter).export(project, options, engine, writer);
            } else if (exporter instanceof StreamExporter) {
                ((StreamExporter) exporter).export(project, options, engine, response.getOutputStream());
            } else if (exporter instanceof StreamExporter) {
                ((StreamExporter) exporter).export(project, options, engine, response.getOutputStream());
            } else {
                respondException(response, new RuntimeException("Unknown exporter type"));
            }
        } catch (Exception e) {
            respondException(response, e);
        } finally {
            ProjectManager.singleton.setBusy(false);
        }
    }
}