# wikipediareader

# Table of contents
1. [Introduction](#introduction)
2. [Installation](#installation)
3. [Interface](#interface)
  1. [Graphical User Interface](#gui)
  2. [Voice User Interface](#vui)

# Introduction
<div id='introduction'/>
The [Spoken Wikipedia project](http://en.wikipedia.org/wiki/Wikipedia:WikiProject_Spoken_Wikipedia) unites volunteer readers of encyclopedic entries. Their recordings make encyclopedic knowledge accessible to persons who are unable to read (out of alexia, visual impairment, or because their sight is currently occupied, e. g. while driving). However, on Wikipedia, recordings are available as raw audio files that can only be consumed linearly, without the possibility for targeted navigation or search. We present a reading application which uses an alignment between the recording, text and article structure and which allows to navigate spoken articles, through a graphical or voice-based user interface (or a combination thereof).

Roughly a thousand articles for each of English, German and Dutch are available, each totalling around 300 hours of speech (with smaller amounts in another 25 languages). This data has recently been made accessible by [Köhn et al.](https://nats-www.informatik.uni-hamburg.de/SWC/) who automatically aligned the audio recordings to their respective article texts using speech recognition technology. Using these alignments, we are able to relate what parts of the article are spoken at any moment in the recordings.

# Installation
<div id='installation'/>

# Interface
<div id='interface'/>

## Graphical User Interface
<div id='gui'/>
![Test](/Sonstiges/gui.png)

The graphical user interface of our full application, including forward/backward jumps between articles (magenta), article search (cyan) and within-articl search (green), the responsive table of contents (blue), a responsive list of links in the surrounding of what is
being read (red), some textual status information (yellow), sliders indicating the relative position in the article (brown), buttons for standard audio navigation (forward/backward/pause), for listening to the table of contents, and for voice-based interaction (purple), and finally buttons to navigate based on article structure: by chapter,
paragraph, sentence, or jumping ahead/back by 10 seconds per click (black). In the experiments, only parts of the interface are available to the users.

## Voice User Interface
<div id='vui'/>
The voice user interface for navigating spoken articles consists of speech activation, recognition and rule-based language understanding with the aim of offering similar functionality as the graphical interface. The user presses and holds down the only button in the interface to activate speech recognition. When the button is released, we decode the recording using Google’s freely available [Speech API](https://cloud.google.com/speech/). For robustness, patterns only need to match parts of what was spoken.
Users may say (variations of) the following:
* “[show me the] [table of] contents”,
* “next/previous chapter/section/paragraph/sentence”,
* “[go back to the] beginning of the chapter/section/
paragraph/sentence” (or simply “repeat”),
* “[go to] chapter/section/subsection N”,
* “section name” to go to the named section,
* “article name” to follow a link or search an article
