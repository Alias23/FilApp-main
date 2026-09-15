package com.example.filapp.controlador;

import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.example.filapp.modelo.Fichero;

import java.io.File;
import java.io.OutputStream;
import java.util.ArrayList;

public class GestorFichero {

    private static GestorFichero miGestorFichero;
    private ArrayList<Fichero> listaFicheros;

    private GestorFichero(){
        this.listaFicheros = new ArrayList<>();
    }
    public static GestorFichero getMiGestorFichero() {
        if (miGestorFichero==null){
            miGestorFichero = new GestorFichero();
        }
        return miGestorFichero;
    }

    public void setFichero(String nom, byte[] cont, String formato){
        Fichero f = new Fichero(nom,cont,formato);
        listaFicheros.add(f);
    }

    public Fichero getFichero(String nom){
        for (int i=0;i<listaFicheros.size();i++){
            Fichero f = listaFicheros.get(i);
            if (f.getNom().equals(nom)){
                return f;
            }
        }
        return null;
    }

    public void convertirFichero(Fichero f, String form) throws IOException{
        /*if(f.getFormato().equals("pdf")) {
            try (PDDocument document = new PDDocument()) {
                PDPage page = new PDPage();
                document.addPage(page);

                // Crear un contenido para escribir en la página
                PDPageContentStream contentStream = new PDPageContentStream(document, page);

                // Convertir los bytes a una cadena para escribir en el PDF
                String text = new String(f.getContenido());

                // Escribir el texto en la página
                PDType1Font fuente = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
                contentStream.setFont(fuente, 12);
                contentStream.beginText();
                contentStream.newLineAtOffset(100, 700); // Posición de inicio del texto
                contentStream.showText(text);
                contentStream.endText();

                // Cerrar el contenido
                contentStream.close();

                // Guardar el documento PDF en el archivo de salida
                document.save(new File(f.getNom()));


                PDFRenderer pdfRenderer = new PDFRenderer(document);
                String outputDir = f.getNom().split(".")[0];
                if(form.equals("jpg")) {
                    // Iterar sobre cada página del PDF
                    for (int pageIndex = 0; pageIndex < document.getNumberOfPages(); pageIndex++) {
                        // Renderizar la página como una imagen BufferedImage
                        BufferedImage image = pdfRenderer.renderImageWithDPI(pageIndex, 300, ImageType.RGB);

                        // Guardar la imagen como un archivo JPEG
                        File outputFile = new File(outputDir, "page_" + (pageIndex + 1) + ".jpg");
                        OutputStream outputStream = new FileOutputStream(outputFile);
                        ImageIO.write(convertToRenderedImage(image), "jpg", outputStream);
                    }
                }
            }
        }*/

    }

    private static RenderedImage convertToRenderedImage(BufferedImage bufferedImage) {
        return (RenderedImage) bufferedImage;
    }

    // Método para escribir BufferedImage en un archivo JPEG
    private static void writeBufferedImageAsJPEG(BufferedImage image, File outputFile) throws IOException {
        try (OutputStream outputStream = new FileOutputStream(outputFile)) {
            ImageIO.write(convertToRenderedImage(image), "jpg", outputStream);
        }
    }

}
