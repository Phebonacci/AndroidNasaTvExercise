package com.builds.phebe.androidnasatv.apiclient.models

/**
 * Created by tempophebe on 02/10/2017.
 */
enum class ChannelStatus {
    CONNECTING {
        override fun toString(): String {
            return "connecting"
        }
    },
    LIVE {
        override fun toString(): String {
            return "live"
        }
    },
    OFF_AIR {
        override fun toString(): String {
            return "offair"
        }
    }
}