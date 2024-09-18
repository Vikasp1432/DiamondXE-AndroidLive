package com.diamondxe.Utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TimeZoneCountryCodeMapper {

    private static final Map<String, String> timezoneToCountryCodeMap = new HashMap<>();
    private static final Set<String> europeanCountries = new HashSet<>();
    private static final String DEFAULT_COUNTRY_CODE = "IN"; // Default country code

    static {
        timezoneToCountryCodeMap.put("America/Adak", "US");
        timezoneToCountryCodeMap.put("America/Anchorage", "US");
        timezoneToCountryCodeMap.put("America/Boise", "US");
        timezoneToCountryCodeMap.put("America/Chicago", "US");
        timezoneToCountryCodeMap.put("America/Denver", "US");
        timezoneToCountryCodeMap.put("America/Detroit", "US");
        timezoneToCountryCodeMap.put("America/Indiana/Indianapolis", "US");
        timezoneToCountryCodeMap.put("America/Indiana/Knox", "US");
        timezoneToCountryCodeMap.put("America/Indiana/Marengo", "US");
        timezoneToCountryCodeMap.put("America/Indiana/Petersburg", "US");
        timezoneToCountryCodeMap.put("America/Indiana/Tell_City", "US");
        timezoneToCountryCodeMap.put("America/Indiana/Vevay", "US");
        timezoneToCountryCodeMap.put("America/Indiana/Vincennes", "US");
        timezoneToCountryCodeMap.put("America/Indiana/Winamac", "US");

        timezoneToCountryCodeMap.put("America/Juneau", "US");
        timezoneToCountryCodeMap.put("America/Kentucky/Louisville", "US");
        timezoneToCountryCodeMap.put("America/Kentucky/Monticello", "US");
        timezoneToCountryCodeMap.put("America/Los_Angeles", "US");
        timezoneToCountryCodeMap.put("America/Menominee", "US");
        timezoneToCountryCodeMap.put("America/Metlakatla", "US");
        timezoneToCountryCodeMap.put("America/New_York", "US");
        timezoneToCountryCodeMap.put("America/Nome", "US");
        timezoneToCountryCodeMap.put("America/North_Dakota/Beulah", "US");
        timezoneToCountryCodeMap.put("America/North_Dakota/Center", "US");
        timezoneToCountryCodeMap.put("America/North_Dakota/New_Salem", "US");

        timezoneToCountryCodeMap.put("America/Phoenix", "US");
        timezoneToCountryCodeMap.put("America/Sitka", "US");
        timezoneToCountryCodeMap.put("America/Yakutat", "US");
        timezoneToCountryCodeMap.put("Pacific/Honolulu", "US");
        timezoneToCountryCodeMap.put("Europe/London", "GB");
        timezoneToCountryCodeMap.put("Asia/Dubai", "AE");
        timezoneToCountryCodeMap.put("Asia/Singapore", "SG");
        timezoneToCountryCodeMap.put("Pacific/Auckland", "NZ");
        timezoneToCountryCodeMap.put("Pacific/Chatham", "NZ");
        timezoneToCountryCodeMap.put("Asia/Calcutta", "IN");
        timezoneToCountryCodeMap.put("Asia/Kolkata", "IN");
        timezoneToCountryCodeMap.put("America/Atikokan", "CA");
        timezoneToCountryCodeMap.put("America/Blanc", "CA");
        timezoneToCountryCodeMap.put("America/Cambridge_Bay", "CA");
        timezoneToCountryCodeMap.put("America/Creston", "CA");
        timezoneToCountryCodeMap.put("America/Dawson", "CA");
        timezoneToCountryCodeMap.put("America/Dawson_Creek", "CA");
        timezoneToCountryCodeMap.put("America/Edmonton", "CA");
        timezoneToCountryCodeMap.put("America/Fort_Nelson", "CA");
        timezoneToCountryCodeMap.put("America/Glace_Bay", "CA");
        timezoneToCountryCodeMap.put("America/Goose_Bay", "CA");
        timezoneToCountryCodeMap.put("America/Halifax", "CA");
        timezoneToCountryCodeMap.put("America/Inuvik", "CA");
        timezoneToCountryCodeMap.put("America/Iqaluit", "CA");
        timezoneToCountryCodeMap.put("America/Moncton", "CA");
        timezoneToCountryCodeMap.put("America/Nipigon", "CA");
        timezoneToCountryCodeMap.put("America/Pangnirtung", "CA");
        timezoneToCountryCodeMap.put("America/Rainy_River", "CA");
        timezoneToCountryCodeMap.put("America/Rankin_Inlet", "CA");
        timezoneToCountryCodeMap.put("America/Regina", "CA");
        timezoneToCountryCodeMap.put("America/Resolute", "CA");
        timezoneToCountryCodeMap.put("America/St_Johns", "CA");
        timezoneToCountryCodeMap.put("America/Swift_Current", "CA");
        timezoneToCountryCodeMap.put("America/Thunder_Bay", "CA");
        timezoneToCountryCodeMap.put("America/Toronto", "CA");
        timezoneToCountryCodeMap.put("America/Vancouver", "CA");
        timezoneToCountryCodeMap.put("America/Whitehorse", "CA");
        timezoneToCountryCodeMap.put("America/Winnipeg", "CA");
        timezoneToCountryCodeMap.put("America/Yellowknife", "CA");
        timezoneToCountryCodeMap.put("Antarctica/Macquarie", "AU");
        timezoneToCountryCodeMap.put("Australia/Adelaide", "AU");
        timezoneToCountryCodeMap.put("Australia/Brisbane", "AU");
        timezoneToCountryCodeMap.put("Australia/Broken_Hill", "AU");
        timezoneToCountryCodeMap.put("Australia/Currie", "AU");
        timezoneToCountryCodeMap.put("Australia/Darwin", "AU");
        timezoneToCountryCodeMap.put("Australia/Eucla", "AU");
        timezoneToCountryCodeMap.put("Australia/Hobart", "AU");
        timezoneToCountryCodeMap.put("Australia/Lindeman", "AU");
        timezoneToCountryCodeMap.put("Australia/Lord_Howe", "AU");
        timezoneToCountryCodeMap.put("Australia/Melbourne", "AU");
        timezoneToCountryCodeMap.put("Australia/Perth", "AU");
        timezoneToCountryCodeMap.put("Australia/Sydney", "AU");
        timezoneToCountryCodeMap.put("Asia/Kabul", "AF");
        timezoneToCountryCodeMap.put("Europe/Mariehamn", "AX");
        timezoneToCountryCodeMap.put("Europe/Tirane", "AL");
        timezoneToCountryCodeMap.put("Africa/Algiers", "DZ");
        timezoneToCountryCodeMap.put("Pacific/Pago_Pago", "AS");
        timezoneToCountryCodeMap.put("Europe/Andorra", "AD");
        timezoneToCountryCodeMap.put("Africa/Luanda", "AO");
        timezoneToCountryCodeMap.put("America/Anguilla", "AI");
        timezoneToCountryCodeMap.put("Antarctica/Casey", "AQ");
        timezoneToCountryCodeMap.put("Antarctica/Davis", "AQ");
        timezoneToCountryCodeMap.put("Antarctica/DumontDUrville", "AQ");
        timezoneToCountryCodeMap.put("Antarctica/Mawson", "AQ");
        timezoneToCountryCodeMap.put("Antarctica/McMurdo", "AQ");
        timezoneToCountryCodeMap.put("Antarctica/Palmer", "AQ");
        timezoneToCountryCodeMap.put("Antarctica/Rothera", "AQ");
        timezoneToCountryCodeMap.put("Antarctica/Syowa", "AQ");
        timezoneToCountryCodeMap.put("Antarctica/Troll", "AQ");
        timezoneToCountryCodeMap.put("Antarctica/Vostok", "AQ");
        timezoneToCountryCodeMap.put("America/Antigua", "AG");
        timezoneToCountryCodeMap.put("America/Argentina/Buenos_Aires", "AR");
        timezoneToCountryCodeMap.put("America/Argentina/Catamarca", "AR");
        timezoneToCountryCodeMap.put("America/Argentina/Cordoba", "AR");
        timezoneToCountryCodeMap.put("America/Argentina/Jujuy", "AR");
        timezoneToCountryCodeMap.put("America/Argentina/La_Rioja", "AR");
        timezoneToCountryCodeMap.put("America/Argentina/Mendoza", "AR");
        timezoneToCountryCodeMap.put("America/Argentina/Rio_Gallegos", "AR");
        timezoneToCountryCodeMap.put("America/Argentina/Salta", "AR");
        timezoneToCountryCodeMap.put("America/Argentina/San_Juan", "AR");
        timezoneToCountryCodeMap.put("America/Argentina/San_Luis", "AR");
        timezoneToCountryCodeMap.put("America/Argentina/Tucuman", "AR");
        timezoneToCountryCodeMap.put("America/Argentina/Ushuaia", "AR");
        timezoneToCountryCodeMap.put("Asia/Yerevan", "AM");
        timezoneToCountryCodeMap.put("America/Aruba", "AW");
        timezoneToCountryCodeMap.put("Europe/Vienna", "AT");
        timezoneToCountryCodeMap.put("Asia/Baku", "AZ");
        timezoneToCountryCodeMap.put("America/Nassau", "BS");
        timezoneToCountryCodeMap.put("Asia/Bahrain", "BH");
        timezoneToCountryCodeMap.put("Asia/Dhaka", "BD");
        timezoneToCountryCodeMap.put("America/Barbados", "BB");
        timezoneToCountryCodeMap.put("Europe/Minsk", "BY");
        timezoneToCountryCodeMap.put("Europe/Brussels", "BE");
        timezoneToCountryCodeMap.put("America/Belize", "BZ");
        timezoneToCountryCodeMap.put("Africa/Porto-Novo", "BJ");
        timezoneToCountryCodeMap.put("Atlantic/Bermuda", "BM");
        timezoneToCountryCodeMap.put("Asia/Thimphu", "BT");
        timezoneToCountryCodeMap.put("America/La_Paz", "BO");
        timezoneToCountryCodeMap.put("Europe/Sarajevo", "BA");
        timezoneToCountryCodeMap.put("Africa/Gaborone", "BW");
        timezoneToCountryCodeMap.put("Europe/Oslo", "BV");
        timezoneToCountryCodeMap.put("America/Araguaina", "BR");
        timezoneToCountryCodeMap.put("America/Bahia", "BR");
        timezoneToCountryCodeMap.put("America/Belem", "BR");
        timezoneToCountryCodeMap.put("America/Boa_Vista", "BR");
        timezoneToCountryCodeMap.put("America/Campo_Grande", "BR");
        timezoneToCountryCodeMap.put("America/Cuiaba", "BR");
        timezoneToCountryCodeMap.put("America/Eirunepe", "BR");
        timezoneToCountryCodeMap.put("America/Fortaleza", "BR");
        timezoneToCountryCodeMap.put("America/Maceio", "BR");
        timezoneToCountryCodeMap.put("America/Manaus", "BR");
        timezoneToCountryCodeMap.put("America/Noronha", "BR");
        timezoneToCountryCodeMap.put("America/Porto_Velho", "BR");
        timezoneToCountryCodeMap.put("America/Recife", "BR");
        timezoneToCountryCodeMap.put("America/Rio_Branco", "BR");
        timezoneToCountryCodeMap.put("America/Santarem", "BR");
        timezoneToCountryCodeMap.put("America/Sao_Paulo", "BR");
        timezoneToCountryCodeMap.put("Indian/Chagos", "IO");
        timezoneToCountryCodeMap.put("Asia/Brunei", "BN");
        timezoneToCountryCodeMap.put("Europe/Sofia", "BG");
        timezoneToCountryCodeMap.put("Africa/Ouagadougou", "BF");
        timezoneToCountryCodeMap.put("Africa/Bujumbura", "BI");
        timezoneToCountryCodeMap.put("Asia/Phnom_Penh", "KH");
        timezoneToCountryCodeMap.put("Africa/Douala", "CM");
        timezoneToCountryCodeMap.put("Atlantic/Cape_Verde", "CV");
        timezoneToCountryCodeMap.put("America/Cayman", "KY");
        timezoneToCountryCodeMap.put("Africa/Bangui", "CF");
        timezoneToCountryCodeMap.put("Africa/Ndjamena", "TD");
        timezoneToCountryCodeMap.put("America/Punta_Arenas", "CL");
        timezoneToCountryCodeMap.put("America/Santiago", "CL");
        timezoneToCountryCodeMap.put("Pacific/Easter", "CL");
        timezoneToCountryCodeMap.put("Asia/Shanghai", "CN");
        timezoneToCountryCodeMap.put("Asia/Urumqi", "CN");
        timezoneToCountryCodeMap.put("Indian/Christmas", "CX");
        timezoneToCountryCodeMap.put("Indian/Cocos", "CC");
        timezoneToCountryCodeMap.put("America/Bogota", "CO");
        timezoneToCountryCodeMap.put("Indian/Comoro", "KM");
        timezoneToCountryCodeMap.put("Africa/Brazzaville", "CG");
        timezoneToCountryCodeMap.put("Africa/Kinshasa", "CD");
        timezoneToCountryCodeMap.put("Africa/Lubumbashi", "CD");
        timezoneToCountryCodeMap.put("Pacific/Rarotonga", "CK");
        timezoneToCountryCodeMap.put("America/Costa_Rica", "CR");
        timezoneToCountryCodeMap.put("Africa/Abidjan", "CI");
        timezoneToCountryCodeMap.put("Europe/Zagreb", "HR");
        timezoneToCountryCodeMap.put("America/Havana", "CU");
        timezoneToCountryCodeMap.put("Asia/Famagusta", "CY");
        timezoneToCountryCodeMap.put("Asia/Nicosia", "CY");
        timezoneToCountryCodeMap.put("Europe/Prague", "CZ");
        timezoneToCountryCodeMap.put("Europe/Copenhagen", "DK");
        timezoneToCountryCodeMap.put("Africa/Djibouti", "DJ");
        timezoneToCountryCodeMap.put("America/Dominica", "DM");
        timezoneToCountryCodeMap.put("America/Santo_Domingo", "DO");
        timezoneToCountryCodeMap.put("Asia/Dili", "TL");
        timezoneToCountryCodeMap.put("America/Guayaquil", "EC");
        timezoneToCountryCodeMap.put("Pacific/Galapagos", "EC");
        timezoneToCountryCodeMap.put("Africa/Cairo", "EG");
        timezoneToCountryCodeMap.put("America/El_Salvador", "SV");
        timezoneToCountryCodeMap.put("Africa/Malabo", "GQ");
        timezoneToCountryCodeMap.put("Africa/Asmara", "ER");
        timezoneToCountryCodeMap.put("Europe/Tallinn", "EE");
        timezoneToCountryCodeMap.put("Africa/Addis_Ababa", "ET");
        timezoneToCountryCodeMap.put("Atlantic/Stanley", "FK");
        timezoneToCountryCodeMap.put("Atlantic/Faroe", "FO");
        timezoneToCountryCodeMap.put("Pacific/Fiji", "FJ");
        timezoneToCountryCodeMap.put("Europe/Helsinki", "FI");
        timezoneToCountryCodeMap.put("Europe/Paris", "FR");
        timezoneToCountryCodeMap.put("America/Cayenne", "GF");
        timezoneToCountryCodeMap.put("Pacific/Gambier", "PF");
        timezoneToCountryCodeMap.put("Pacific/Marquesas", "PF");
        timezoneToCountryCodeMap.put("Pacific/Tahiti", "PF");
        timezoneToCountryCodeMap.put("Indian/Kerguelen", "TF");
        timezoneToCountryCodeMap.put("Africa/Libreville", "GA");
        timezoneToCountryCodeMap.put("Africa/Banjul", "GM");
        timezoneToCountryCodeMap.put("Asia/Tbilisi", "GE");
        timezoneToCountryCodeMap.put("Europe/Berlin", "DE");
        timezoneToCountryCodeMap.put("Europe/Busingen", "DE");
        timezoneToCountryCodeMap.put("Africa/Accra", "GH");
        timezoneToCountryCodeMap.put("Europe/Gibraltar", "GI");
        timezoneToCountryCodeMap.put("Europe/Athens", "GR");
        timezoneToCountryCodeMap.put("America/Danmarkshavn", "GL");
        timezoneToCountryCodeMap.put("America/Nuuk", "GL");
        timezoneToCountryCodeMap.put("America/Scoresbysund", "GL");
        timezoneToCountryCodeMap.put("America/Thule", "GL");
        timezoneToCountryCodeMap.put("America/Grenada", "GD");
        timezoneToCountryCodeMap.put("America/Guadeloupe", "GP");
        timezoneToCountryCodeMap.put("Pacific/Guam", "GU");
        timezoneToCountryCodeMap.put("America/Guatemala", "GT");
        timezoneToCountryCodeMap.put("Europe/Guernsey", "GG");
        timezoneToCountryCodeMap.put("Africa/Conakry", "GN");
        timezoneToCountryCodeMap.put("Africa/Bissau", "GW");
        timezoneToCountryCodeMap.put("America/Guyana", "GY");
        timezoneToCountryCodeMap.put("America/Port-au-Prince","HT");
        timezoneToCountryCodeMap.put("America/Tegucigalpa", "HN");
        timezoneToCountryCodeMap.put("Asia/Hong_Kong", "HK");
        timezoneToCountryCodeMap.put("Europe/Budapest", "HU");
        timezoneToCountryCodeMap.put("Atlantic/Reykjavik", "IS");
        timezoneToCountryCodeMap.put("Asia/Jakarta", "ID");
        timezoneToCountryCodeMap.put("Asia/Jayapura", "ID");
        timezoneToCountryCodeMap.put("Asia/Makassar", "ID");
        timezoneToCountryCodeMap.put("Asia/Pontianak", "ID");
        timezoneToCountryCodeMap.put("Asia/Tehran", "IR");
        timezoneToCountryCodeMap.put("Asia/Baghdad", "IQ");
        timezoneToCountryCodeMap.put("Europe/Dublin", "IE");
        timezoneToCountryCodeMap.put("Asia/Jerusalem", "IL");
        timezoneToCountryCodeMap.put("Europe/Rome", "IT");
        timezoneToCountryCodeMap.put("America/Jamaica", "JM");
        timezoneToCountryCodeMap.put("Asia/Tokyo", "JP");
        timezoneToCountryCodeMap.put("Europe/Jersey", "JE");
        timezoneToCountryCodeMap.put("Asia/Amman", "JO");
        timezoneToCountryCodeMap.put("Asia/Almaty", "KZ");
        timezoneToCountryCodeMap.put("Asia/Aqtau", "KZ");
        timezoneToCountryCodeMap.put("Asia/Aqtobe", "KZ");
        timezoneToCountryCodeMap.put("Asia/Atyrau", "KZ");
        timezoneToCountryCodeMap.put("Asia/Oral", "KZ");
        timezoneToCountryCodeMap.put("Asia/Qostanay", "KZ");
        timezoneToCountryCodeMap.put("Asia/Qyzylorda", "KZ");
        timezoneToCountryCodeMap.put("Africa/Nairobi", "KE");
        timezoneToCountryCodeMap.put("Pacific/Enderbury", "KI");
        timezoneToCountryCodeMap.put("Pacific/Kiritimati", "KI");
        timezoneToCountryCodeMap.put("Pacific/Tarawa", "KI");
        timezoneToCountryCodeMap.put("Asia/Pyongyang", "KP");
        timezoneToCountryCodeMap.put("Asia/Seoul", "KR");
        timezoneToCountryCodeMap.put("Asia/Kuwait", "KW");
        timezoneToCountryCodeMap.put("Asia/Bishkek", "KG");
        timezoneToCountryCodeMap.put("Asia/Vientiane", "LA");
        timezoneToCountryCodeMap.put("Europe/Riga", "LV");
        timezoneToCountryCodeMap.put("Asia/Beirut", "LB");
        timezoneToCountryCodeMap.put("Africa/Maseru", "LS");
        timezoneToCountryCodeMap.put("Africa/Monrovia", "LR");
        timezoneToCountryCodeMap.put("Africa/Tripoli", "LY");
        timezoneToCountryCodeMap.put("Europe/Vaduz", "LI");
        timezoneToCountryCodeMap.put("Europe/Vilnius", "LT");
        timezoneToCountryCodeMap.put("Europe/Luxembourg", "LU");
        timezoneToCountryCodeMap.put("Asia/Macau", "MO");
        timezoneToCountryCodeMap.put("Europe/Skopje", "MK");
        timezoneToCountryCodeMap.put("Indian/Antananarivo", "MG");
        timezoneToCountryCodeMap.put("Africa/Blantyre", "MW");
        timezoneToCountryCodeMap.put("Asia/Kuala_Lumpur", "MY");
        timezoneToCountryCodeMap.put("Asia/Kuching", "MY");
        timezoneToCountryCodeMap.put("Indian/Maldives", "MV");
        timezoneToCountryCodeMap.put("Africa/Bamako", "ML");
        timezoneToCountryCodeMap.put("Europe/Malta", "MT");
        timezoneToCountryCodeMap.put("Europe/Isle_of_Man", "IM");
        timezoneToCountryCodeMap.put("Pacific/Kwajalein", "MH");
        timezoneToCountryCodeMap.put("Pacific/Majuro", "MH");
        timezoneToCountryCodeMap.put("America/Martinique", "MQ");
        timezoneToCountryCodeMap.put("Africa/Nouakchott", "MR");
        timezoneToCountryCodeMap.put("Indian/Mauritius", "MU");
        timezoneToCountryCodeMap.put("Indian/Mayotte", "YT");
        timezoneToCountryCodeMap.put("America/Bahia_Banderas", "MX");
        timezoneToCountryCodeMap.put("America/Cancun", "MX");
        timezoneToCountryCodeMap.put("America/Chihuahua", "MX");
        timezoneToCountryCodeMap.put("America/Hermosillo", "MX");
        timezoneToCountryCodeMap.put("America/Matamoros", "MX");
        timezoneToCountryCodeMap.put("America/Mazatlan", "MX");
        timezoneToCountryCodeMap.put("America/Merida", "MX");
        timezoneToCountryCodeMap.put("America/Mexico_City", "MX");
        timezoneToCountryCodeMap.put("America/Monterrey", "MX");
        timezoneToCountryCodeMap.put("America/Ojinaga", "MX");
        timezoneToCountryCodeMap.put("America/Tijuana", "MX");
        timezoneToCountryCodeMap.put("Pacific/Chuuk", "FM");
        timezoneToCountryCodeMap.put("Pacific/Kosrae", "FM");
        timezoneToCountryCodeMap.put("Pacific/Pohnpei", "FM");
        timezoneToCountryCodeMap.put("Europe/Chisinau", "MD");
        timezoneToCountryCodeMap.put("Europe/Monaco", "MC");
        timezoneToCountryCodeMap.put("Asia/Choibalsan", "MN");
        timezoneToCountryCodeMap.put("Asia/Hovd", "MN");
        timezoneToCountryCodeMap.put("Asia/Ulaanbaatar", "MN");
        timezoneToCountryCodeMap.put("Europe/Podgorica", "ME");
        timezoneToCountryCodeMap.put("America/Montserrat", "MS");
        timezoneToCountryCodeMap.put("Africa/Casablanca", "MA");
        timezoneToCountryCodeMap.put("Africa/Maputo", "MZ");
        timezoneToCountryCodeMap.put("Asia/Yangon", "MM");
        timezoneToCountryCodeMap.put("Africa/Windhoek", "NA");
        timezoneToCountryCodeMap.put("Pacific/Nauru", "NR");
        timezoneToCountryCodeMap.put("Asia/Kathmandu", "NP");
        timezoneToCountryCodeMap.put("Europe/Amsterdam", "NL");
        timezoneToCountryCodeMap.put("Pacific/Noumea", "NC");
        timezoneToCountryCodeMap.put("America/Managua", "NI");
        timezoneToCountryCodeMap.put("Africa/Niamey", "NE");
        timezoneToCountryCodeMap.put("Africa/Lagos", "NG");
        timezoneToCountryCodeMap.put("Pacific/Niue", "NU");
        timezoneToCountryCodeMap.put("Pacific/Norfolk", "NF");
        timezoneToCountryCodeMap.put("Pacific/Saipan", "MP");
        timezoneToCountryCodeMap.put("Asia/Muscat", "OM");
        timezoneToCountryCodeMap.put("Asia/Karachi", "PK");
        timezoneToCountryCodeMap.put("Pacific/Palau", "PW");
        timezoneToCountryCodeMap.put("Asia/Gaza", "PS");
        timezoneToCountryCodeMap.put("Asia/Hebron", "PS");
        timezoneToCountryCodeMap.put("America/Panama", "PA");
        timezoneToCountryCodeMap.put("Pacific/Bougainville", "PG");
        timezoneToCountryCodeMap.put("Pacific/Port_Moresby", "PG");
        timezoneToCountryCodeMap.put("America/Asuncion", "PY");
        timezoneToCountryCodeMap.put("America/Lima", "PE");
        timezoneToCountryCodeMap.put("Asia/Manila", "PH");
        timezoneToCountryCodeMap.put("Pacific/Pitcairn", "PN");
        timezoneToCountryCodeMap.put("Europe/Warsaw", "PL");
        timezoneToCountryCodeMap.put("Atlantic/Azores", "PT");
        timezoneToCountryCodeMap.put("Atlantic/Madeira", "PT");
        timezoneToCountryCodeMap.put("Europe/Lisbon", "PT");
        timezoneToCountryCodeMap.put("America/Puerto_Rico", "PR");
        timezoneToCountryCodeMap.put("Asia/Qatar", "QA");
        timezoneToCountryCodeMap.put("Indian/Reunion", "RE");
        timezoneToCountryCodeMap.put("Europe/Bucharest", "RO");
        timezoneToCountryCodeMap.put("Asia/Anadyr", "RU");
        timezoneToCountryCodeMap.put("Asia/Barnaul", "RU");
        timezoneToCountryCodeMap.put("Asia/Chita", "RU");
        timezoneToCountryCodeMap.put("Asia/Irkutsk", "RU");
        timezoneToCountryCodeMap.put("Asia/Kamchatka", "RU");
        timezoneToCountryCodeMap.put("Asia/Khandyga", "RU");
        timezoneToCountryCodeMap.put("Asia/Krasnoyarsk", "RU");
        timezoneToCountryCodeMap.put("Asia/Magadan", "RU");
        timezoneToCountryCodeMap.put("Asia/Novokuznetsk", "RU");
        timezoneToCountryCodeMap.put("Asia/Novosibirsk", "RU");
        timezoneToCountryCodeMap.put("Asia/Omsk", "RU");
        timezoneToCountryCodeMap.put("Asia/Sakhalin", "RU");
        timezoneToCountryCodeMap.put("Asia/Srednekolymsk", "RU");
        timezoneToCountryCodeMap.put("Asia/Tomsk", "RU");
        timezoneToCountryCodeMap.put("Asia/Ust", "RU");
        timezoneToCountryCodeMap.put("Asia/Vladivostok", "RU");
        timezoneToCountryCodeMap.put("Asia/Yakutsk", "RU");
        timezoneToCountryCodeMap.put("Asia/Yekaterinburg", "RU");
        timezoneToCountryCodeMap.put("Europe/Astrakhan", "RU");
        timezoneToCountryCodeMap.put("Europe/Kaliningrad", "RU");
        timezoneToCountryCodeMap.put("Europe/Kirov", "RU");
        timezoneToCountryCodeMap.put("Europe/Moscow", "RU");
        timezoneToCountryCodeMap.put("Europe/Samara", "RU");
        timezoneToCountryCodeMap.put("Europe/Saratov", "RU");
        timezoneToCountryCodeMap.put("Europe/Ulyanovsk", "RU");
        timezoneToCountryCodeMap.put("Europe/Volgograd", "RU");
        timezoneToCountryCodeMap.put("Africa/Kigali", "RW");
        timezoneToCountryCodeMap.put("Atlantic/St_Helena", "SH");
        timezoneToCountryCodeMap.put("America/St_Kitts", "KN");
        timezoneToCountryCodeMap.put("America/St_Lucia", "LC");
        timezoneToCountryCodeMap.put("America/Miquelon", "PM");
        timezoneToCountryCodeMap.put("America/St_Vincent", "VC");
        timezoneToCountryCodeMap.put("America/St_Barthelemy", "BL");
        timezoneToCountryCodeMap.put("America/Marigot", "MF");
        timezoneToCountryCodeMap.put("Pacific/Apia", "WS");
        timezoneToCountryCodeMap.put("Europe/San_Marino", "SM");
        timezoneToCountryCodeMap.put("Africa/Sao_Tome", "ST");
        timezoneToCountryCodeMap.put("Asia/Riyadh", "SA");
        timezoneToCountryCodeMap.put("Africa/Dakar", "SN");
        timezoneToCountryCodeMap.put("Europe/Belgrade", "RS");
        timezoneToCountryCodeMap.put("Indian/Mahe", "SC");
        timezoneToCountryCodeMap.put("Africa/Freetown", "SL");
        timezoneToCountryCodeMap.put("Europe/Bratislava", "SK");
        timezoneToCountryCodeMap.put("Europe/Ljubljana", "SI");
        timezoneToCountryCodeMap.put("Pacific/Guadalcanal", "SB");
        timezoneToCountryCodeMap.put("Africa/Mogadishu", "SO");
        timezoneToCountryCodeMap.put("Africa/Johannesburg", "ZA");
        timezoneToCountryCodeMap.put("Atlantic/South_Georgia", "GS");
        timezoneToCountryCodeMap.put("Africa/Juba", "SS");
        timezoneToCountryCodeMap.put("Africa/Ceuta", "ES");
        timezoneToCountryCodeMap.put("Atlantic/Canary", "ES");
        timezoneToCountryCodeMap.put("Europe/Madrid", "ES");
        timezoneToCountryCodeMap.put("Asia/Colombo", "LK");
        timezoneToCountryCodeMap.put("Africa/Khartoum", "SD");
        timezoneToCountryCodeMap.put("America/Paramaribo", "SR");
        timezoneToCountryCodeMap.put("Arctic/Longyearbyen", "SJ");
        timezoneToCountryCodeMap.put("Africa/Mbabane", "SZ");
        timezoneToCountryCodeMap.put("Europe/Stockholm", "SE");
        timezoneToCountryCodeMap.put("Europe/Zurich", "CH");
        timezoneToCountryCodeMap.put("Asia/Damascus", "SY");
        timezoneToCountryCodeMap.put("Asia/Taipei", "TW");
        timezoneToCountryCodeMap.put("Asia/Dushanbe", "TJ");
        timezoneToCountryCodeMap.put("Africa/Dar_es_Salaam", "TZ");
        timezoneToCountryCodeMap.put("Asia/Bangkok", "TH");
        timezoneToCountryCodeMap.put("Africa/Lome", "TG");
        timezoneToCountryCodeMap.put("Pacific/Fakaofo", "TK");
        timezoneToCountryCodeMap.put("Pacific/Tongatapu", "TO");
        timezoneToCountryCodeMap.put("America/Port_of_Spain", "TT");
        timezoneToCountryCodeMap.put("Africa/Tunis", "TN");
        timezoneToCountryCodeMap.put("Europe/Istanbul", "TR");
        timezoneToCountryCodeMap.put("Asia/Ashgabat", "TM");
        timezoneToCountryCodeMap.put("America/Grand_Turk", "TC");
        timezoneToCountryCodeMap.put("Pacific/Funafuti", "TV");
        timezoneToCountryCodeMap.put("Africa/Kampala", "UG");
        timezoneToCountryCodeMap.put("Europe/Kiev", "UA");
        timezoneToCountryCodeMap.put("Europe/Simferopol", "UA");
        timezoneToCountryCodeMap.put("Europe/Uzhgorod", "UA");
        timezoneToCountryCodeMap.put("Europe/Zaporozhye", "UA");
        timezoneToCountryCodeMap.put("Pacific/Midway", "UM");
        timezoneToCountryCodeMap.put("Pacific/Wake", "UM");
        timezoneToCountryCodeMap.put("America/Montevideo", "UY");
        timezoneToCountryCodeMap.put("Asia/Samarkand", "UZ");
        timezoneToCountryCodeMap.put("Asia/Tashkent", "UZ");
        timezoneToCountryCodeMap.put("Pacific/Efate", "VU");
        timezoneToCountryCodeMap.put("Europe/Vatican", "VA");
        timezoneToCountryCodeMap.put("America/Caracas", "VE");
        timezoneToCountryCodeMap.put("Asia/Ho_Chi_Minh", "VN");
        timezoneToCountryCodeMap.put("America/Tortola", "VG");
        timezoneToCountryCodeMap.put("America/St_Thomas", "VI");
        timezoneToCountryCodeMap.put("Pacific/Wallis", "WF");
        timezoneToCountryCodeMap.put("Africa/El_Aaiun", "EH");
        timezoneToCountryCodeMap.put("Asia/Aden", "YE");
        timezoneToCountryCodeMap.put("Africa/Lusaka", "ZM");
        timezoneToCountryCodeMap.put("Africa/Harare", "ZW");
        timezoneToCountryCodeMap.put("America/Curacao", "CW");

        // Add European country codes to the set
        europeanCountries.add("AX");
        europeanCountries.add("AL");
        europeanCountries.add("AD");
        europeanCountries.add("AT");
        europeanCountries.add("BY");
        europeanCountries.add("BE");
        europeanCountries.add("BA");
        europeanCountries.add("BV");
        europeanCountries.add("BG");
        europeanCountries.add("HR");
        europeanCountries.add("CZ");
        europeanCountries.add("DK");
        europeanCountries.add("EE");
        europeanCountries.add("FI");
        europeanCountries.add("FR");
        europeanCountries.add("DE");
        europeanCountries.add("GI");
        europeanCountries.add("GR");
        europeanCountries.add("GG");
        europeanCountries.add("HU");
        europeanCountries.add("IE");
        europeanCountries.add("IT");
        europeanCountries.add("JE");
        europeanCountries.add("LV");
        europeanCountries.add("LI");
        europeanCountries.add("LT");
        europeanCountries.add("LU");
        europeanCountries.add("MK");
        europeanCountries.add("MT");
        europeanCountries.add("IM");
        europeanCountries.add("MD");
        europeanCountries.add("MC");
        europeanCountries.add("ME");
        europeanCountries.add("NL");
        europeanCountries.add("PL");
        europeanCountries.add("PT");
        europeanCountries.add("RO");
        europeanCountries.add("RU");
        europeanCountries.add("SM");
        europeanCountries.add("RS");
        europeanCountries.add("SK");
        europeanCountries.add("SI");
        europeanCountries.add("ES");
        europeanCountries.add("SE");
        europeanCountries.add("CH");
        europeanCountries.add("TR");
        europeanCountries.add("UA");
        europeanCountries.add("VA");

        // Add more mappings as needed
    }

   /* public static String getCountryCodeFromTimeZone(String timeZoneId) {
        return timezoneToCountryCodeMap.getOrDefault(timeZoneId, "Unknown");
    }*/

    public static String getCountryCodeFromTimeZone(String timeZoneId) {
        String countryCode = timezoneToCountryCodeMap.getOrDefault(timeZoneId, DEFAULT_COUNTRY_CODE);

        if (europeanCountries.contains(countryCode)) {
            return "EU";
        }
        else{
            return countryCode;
        }
    }
}