#include "mainwindow.h"
#include "ui_mainwindow.h"
#include "dialog.h"
#include "globals.h"
#include<QTime>
#include<QString>
#include<QLabel>
#include<QGraphicsScene>
#include<QPixmap>

MainWindow::MainWindow(QWidget *parent) :
    QMainWindow(parent),
    ui(new Ui::MainWindow)
{
    ui->setupUi(this);

    //jonas: setzen der Nachricht
    QTime time = QTime::currentTime();
    QString stime = time.toString();
    nachricht_string = stime + " Nachricht";

    //jonas: setzen des Logos und der Kontaktinformationen
    QGraphicsScene *scene = new QGraphicsScene();
    QPixmap m("Logos/Whatsapp.jpg");
    scene->setBackgroundBrush(m.scaled(50,50,Qt::KeepAspectRatio,Qt::SmoothTransformation));
    ui->gV_logo->setScene(scene);
}

MainWindow::~MainWindow()
{
    delete ui;
}

void MainWindow::on_pB_antworten_clicked()
{
    Dialog dialog;
    dialog.setModal(true);
    this->close(); //jonas: mainwindow zuerst schließen
    dialog.exec();
}




