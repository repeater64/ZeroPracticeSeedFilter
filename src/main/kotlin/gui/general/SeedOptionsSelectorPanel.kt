package gui.general

interface SeedOptionsSelectorPanel {
    fun getNumApplicableSeeds(): Int
    fun getNextSeed(): Long
    fun getInvalidSelectionErrorPopup(): String
    fun getInvalidSelectionWarning(): String
}