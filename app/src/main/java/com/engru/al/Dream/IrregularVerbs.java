package com.engru.al.Dream;

import android.content.ContentValues;

public class IrregularVerbs extends BaseORM{
    int id;
    String infinitive_v1;
    String past_simple_v2;
    String past_particple_v3;
    String ru;
    IrregularVerbs(){
        id=-1;
        infinitive_v1="";
        past_simple_v2="";
        past_particple_v3="";
        ru="";
    }
    static class Table{
        public static final String TABLE_NAME="IrregularVerbs";
        public static final String ID="_id";
        public static final String Infinitive_V1="Infinitive_V1";
        public static final String PAST_SIMPLE_V2="PAST_SIMPLE_V2";
        public static final String PAST_PARTICPLE_V3="PAST_PARTICPLE_V3";
        public static final String RU="ru";
        public static final String ENG_VALUE="eng_value";
        public static final String EXAMPLE="example";
        public static final String CREATE_TABLES="CREATE TABLE "+ TABLE_NAME + " ( "
                + " _id INTEGER PRIMARY KEY, "
                + Infinitive_V1 + " TEXT , "
                + PAST_SIMPLE_V2 + " TEXT, "
                + PAST_PARTICPLE_V3 + " TEXT, "
                + ENG_VALUE + " TEXT, "
                + EXAMPLE + " TEXT, "
                + RU + " TEXT "
                +")";
        public static void insertDefaultValues(){
            // 1 - Infinitive_V1
            // 2 - PAST_SIMPLE_V2
            // 3 - PAST_PARTICPLE_V3
            // 4 - RU
            // 5 - ENG_VALUE
            // 6 - EXAMPLE
            String [][]importantSomeWords={
                    {"be", "was, were", "been", "быть, являться" , " used to show the position of someone or something" , "It's been in the cupboard for months." },
                    {"beat", "beat", "beaten", "бить, колотить" , "to hit against something hard, making a continuous or regular sound" , "Rain beat against the windows." },
                    {"become", "became", "become", "становиться" , " to begin to be something" , "She wants to become a teacher when she leaves school." },
                    {"begin", "began", "begun", "начинать" , " to start to do something" , "She began her career as a journalist on a local newspaper." },
                    {"bend", "bent", "bent", "гнуть / сгибать(ся)" , "to move your body or part of your body so that it is not straight" , "He was bending over to tie his shoelaces." },
                    {"bet", "bet", "bet", "ставить, держать пари" , "to risk money on the result of a game, competition, etc" , "He lost all his money betting on horses." },
                    {"bite", "bit", "bitten", "кусать" , "to cut something using your teeth" , "She bit into an apple. | He was bitten by a dog." },
                    {"blow", "blew", "blown", "дуть, выдыхать" , "to force air out through your mouth" , "She blew on her coffee before taking a sip." },
                    {"break", "broke", "broken", "ломать, разбивать, разрушать" , "to separate into two or more pieces, or to make something separate into two or more pieces" , "The vase fell on the floor and broke." },
                    {"bring", "brought", "brought", "приносить, привозить, доставлять" , "to take someone or something with you when you go somewhere" , "Did you bring an umbrella with you?" },
                    {"build", "built", "built", "строить, сооружать" , "eng_valie" , "example" },
                    {"buy", "bought", "bought", "покупать, приобретать" , "eng_valie" , "example" },
                    {"catch", "caught", "caught", "ловить, поймать, схватить" , "eng_valie" , "example" },
                    {"choose", "chose", "chosen", "выбирать, избирать" , "eng_valie" , "example" },
                    {"come", "came", "come", "приходить, подходить" , "eng_valie" , "example" },
                    {"cost", "cost", "cost", "стоить, обходиться" , "eng_valie" , "example" },
                    {"cut", "cut", "cut", "резать, разрезать" , "eng_valie" , "example" },
                    {"deal", "dealt", "dealt", "иметь дело, распределять" , "eng_valie" , "example" },
                    {"dig", "dug", "dug", "копать, рыть" , "eng_valie" , "example" },
                    {"do", "did", "done", "делать, выполнять" , "eng_valie" , "example" },
                    {"draw", "drew", "drawn", "рисовать, чертить" , "eng_valie" , "example" },
                    {"drink", "drank", "drunk", "пить" , "eng_valie" , "example" },
                    {"drive", "drove", "driven", "ездить, подвозить" , "eng_valie" , "example" },
                    {"eat", "ate", "eaten", "есть, поглощать, поедать" , "eng_valie" , "example" },
                    {"fall", "fell", "fallen", "падать" , "eng_valie" , "example" },
                    {"feed", "fed", "fed", "кормить" , "eng_valie" , "example" },
                    {"feel", "felt", "felt", "чувствовать, ощущать" , "eng_valie" , "example" },
                    {"fight", "fought", "fought", "драться, сражаться, воевать" , "eng_valie" , "example" },
                    {"find", "found", "found", "находить, обнаруживать" , "eng_valie" , "example" },
                    {"fly", "flew", "flown", "летать" , "eng_valie" , "example" },
                    {"forget", "forgot", "forgotten", "забывать о (чём-либо)" , "eng_valie" , "example" },
                    {"forgive", "forgave", "forgiven", "прощать" , "eng_valie" , "example" },
                    {"freeze", "froze", "frozen", "замерзать, замирать" , "eng_valie" , "example" },
                    {"get", "got", "got", "получать, добираться" , "eng_valie" , "example" },
                    {"give", "gave", "given", "дать, подать, дарить" , "eng_valie" , "example" },
                    {"go", "went", "gone", "идти, двигаться" , "eng_valie" , "example" },
                    {"grow", "grew", "grown", "расти, вырастать" , "eng_valie" , "example" },
                    {"hang", "hung", "hung", "вешать, развешивать, висеть" , "eng_valie" , "example" },
                    {"have", "had", "had", "иметь, обладать" , "eng_valie" , "example" },
                    {"hear", "heard", "heard", "слышать, услышать" , "eng_valie" , "example" },
                    {"hide", "hid", "hidden", "прятать, скрывать" , "eng_valie" , "example" },
                    {"hit", "hit", "hit", "ударять, поражать" , "eng_valie" , "example" },
                    {"hold", "held", "held", "держать, удерживать, задерживать" , "eng_valie" , "example" },
                    {"hurt", "hurt", "hurt", "ранить, причинять боль, ушибить" , "eng_valie" , "example" },
                    {"keep", "kept", "kept", "хранить, сохранять, поддерживать" , "eng_valie" , "example" },
                    {"know", "knew", "known", "знать, иметь представление" , "eng_valie" , "example" },
                    {"lay", "laid", "laid", "класть, положить, покрывать" , "eng_valie" , "example" },
                    {"lead", "led", "led", "вести за собой, сопровождать, руководить" , "eng_valie" , "example" },
                    {"leave", "left", "left", "покид��ть, уходить, уезжать, оставлять" , "eng_valie" , "example" },
                    {"lend", "lent", "lent", "одалживать, давать взаймы (в долг)" , "eng_valie" , "example" },
                    {"let", "let", "let", "позволять, разрешать" , "eng_valie" , "example" },
                    {"lie", "lay", "lain", "лежать" , "eng_valie" , "example" },
                    {"light", "lit", "lit", "зажигать, светиться, освещать" , "eng_valie" , "example" },
                    {"lose", "lost", "lost", "терять, лишаться, утрачивать" , "eng_valie" , "example" },
                    {"make", "made", "made", "делать, создавать, изготавливать" , "eng_valie" , "example" },
                    {"mean", "meant", "meant", "значить, иметь в виду, подразумевать" , "eng_valie" , "example" },
                    {"meet", "met", "met", "встречать, знакомиться" , "eng_valie" , "example" },
                    {"pay", "paid", "paid", "платить, оплачивать, рассчитываться" , "eng_valie" , "example" },
                    {"put", "put", "put", "ставить, помещать, класть" , "eng_valie" , "example" },
                    {"read", "read", "read", "читать, прочитать" , "eng_valie" , "example" },
                    {"ride", "rode", "ridden", "ехать верхом, кататься" , "eng_valie" , "example" },
                    {"ring", "rang", "rung", "звенеть, звонить" , "eng_valie" , "example" },
                    {"rise", "rose", "risen", "восходить, вставать, подниматься" , "eng_valie" , "example" },
                    {"run", "ran", "run", "бежать, бегать" , "eng_valie" , "example" },
                    {"say", "said", "said", "говорить, сказать, произносить" , "eng_valie" , "example" },
                    {"see", "saw", "seen", "видеть" , "eng_valie" , "example" },
                    {"seek", "sought", "sought", "искать, разыскивать" , "eng_valie" , "example" },
                    {"sell", "sold", "sold", "продавать, торговать" , "eng_valie" , "example" },
                    {"send", "sent", "sent", "посылать, отправлять, отсылать" , "eng_valie" , "example" },
                    {"set", "set", "set", "устанавливать, задавать, назначать" , "eng_valie" , "example" },
                    {"shake", "shook", "shaken", "трясти, встряхивать" , "eng_valie" , "example" },
                    {"shine", "shone", "shone", "светить, сиять, озарять" , "eng_valie" , "example" },
                    {"shoot", "shot", "shot", "стрелять" , "eng_valie" , "example" },
                    {"show", "showed", "shown, showed", "показывать" , "eng_valie" , "example" },
                    {"shut", "shut", "shut", "закрывать, запирать, затворять" , "eng_valie" , "example" },
                    {"sing", "sang", "sung", "петь, напевать" , "eng_valie" , "example" },
                    {"sink", "sank", "sunk", "тонуть, погружаться" , "eng_valie" , "example" },
                    {"sit", "sat", "sat", "сидеть, садиться" , "eng_valie" , "example" },
                    {"sleep", "slept", "slept", "спать" , "eng_valie" , "example" },
                    {"speak", "spoke", "spoken", "говорить, разговаривать, высказываться" , "eng_valie" , "example" },
                    {"spend", "spent", "spent", "тратить, расходовать, проводить (время)" , "eng_valie" , "example" },
                    {"stand", "stood", "stood", "стоять" , "eng_valie" , "example" },
                    {"steal", "stole", "stolen", "воровать, красть" , "eng_valie" , "example" },
                    {"stick", "stuck", "stuck", "втыкать, приклеивать" , "eng_valie" , "example" },
                    {"strike", "struck", "struck, stricken", "ударять, бить, поражать" , "eng_valie" , "example" },
                    {"swear", "swore", "sworn", "клясться, присягать" , "eng_valie" , "example" },
                    {"sweep", "swept", "swept", "мести, подметать, смахивать" , "eng_valie" , "example" },
                    {"swim", "swam", "swum", "плавать, плыть" , "eng_valie" , "example" },
                    {"swing", "swung", "swung", "качаться, вертеться" , "eng_valie" , "example" },
                    {"take", "took", "taken", "брать, хва��ать, взять" , "eng_valie" , "example" },
                    {"teach", "taught", "taught", "учить, обучать" , "eng_valie" , "example" },
                    {"tear", "tore", "torn", "рвать, отрывать" , "eng_valie" , "example" },
                    {"tell", "told", "told", "рассказывать" , "eng_valie" , "example" },
                    {"think", "thought", "thought", "думать, мыслить, размышлять" , "eng_valie" , "example" },
                    {"throw", "threw", "thrown", "бросать, кидать, метать" , "eng_valie" , "example" },
                    {"understand", "understood", "understood", "понимать, постигать" , "eng_valie" , "example" },
                    {"wake", "woke", "woken", "просыпаться, будить" , "eng_valie" , "example" },
                    {"wear", "wore", "worn", "носить (одежду)" , "eng_valie" , "example" },
                    {"win", "won", "won", "победить, выиграть" , "eng_valie" , "example" },
                    {"write", "wrote", "written", "писать, записывать" , "eng_valie" , "example" }
            };

            get_db().beginTransaction();
            ContentValues fields = new ContentValues();
            for(String[]word:importantSomeWords){
                fields.clear();
                fields.put(IrregularVerbs.Table.Infinitive_V1, word[0]);
                fields.put(IrregularVerbs.Table.PAST_SIMPLE_V2, word[1]);
                fields.put(IrregularVerbs.Table.PAST_PARTICPLE_V3, word[2]);
                fields.put(IrregularVerbs.Table.RU, word[3]);
                fields.put(IrregularVerbs.Table.ENG_VALUE, word[4]);
                fields.put(IrregularVerbs.Table.EXAMPLE, word[5]);
                get_db().insert(IrregularVerbs.Table.TABLE_NAME, null, fields);
            }
            get_db().endTransaction();
        }
    }
}
