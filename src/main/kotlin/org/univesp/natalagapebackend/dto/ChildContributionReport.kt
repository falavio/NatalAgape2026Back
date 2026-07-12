package org.univesp.natalagapebackend.dto


import org.univesp.natalagapebackend.models.Child
import org.univesp.natalagapebackend.models.ChildContribution
import org.univesp.natalagapebackend.models.Color


data class ChildContributionReport(
    val childWithContribution: Int,
    val childWithNoContribution: Int,
    val childWithPendingContribution: Int,
    val totalChildren: Int,
    val childrenWithContributionList: List<ChildrenWithContribution>,
    val childrenWithNoContributionList: List<ChildrenWithNoContribution>,
    val childrenWithPendingContributionList: List<ChildrenWithPendingContribution>
)


data class ChildrenWithContribution(
    val responsibleName: String,
    val childName: String,
    val leaderName: String,
    val leaderColor: Color,
)

data class ChildrenWithNoContribution(
    val responsibleName: String,
    val childName: String,
    val neighborhoodName: String,
    val leaderName: String
)

data class ChildrenWithPendingContribution(
    val responsibleName: String,
    val childName: String,
    val sponsorName: String,
    val sponsorPhone: String,
    val leaderName: String,
    val clothesDelivered: Boolean? = null,
    val toyDelivered: Boolean? = null
)

fun childContributionToDTOReport(
    childContributions: List<ChildContribution>,
    children: List<Child>
): ChildContributionReport {
    val childContributionsWithPending =
        childContributions.filter { (it.wasDelivered == false || it.toyDelivered == false) && it.acceptance == null }
    val childContributionsWithContribution =
        childContributions.filter { it.wasDelivered == true && it.acceptance != null }
    val childContributionsWithNoContribution = children.filter { child ->
        childContributions.none { it.child.childId == child.childId }
    }

    return ChildContributionReport(
        childWithContribution = childContributionsWithContribution.size,
        childWithNoContribution = childContributionsWithNoContribution.size,
        childWithPendingContribution = childContributionsWithPending.size,
        totalChildren = childContributionsWithContribution.size + childContributionsWithNoContribution.size + childContributionsWithPending.size,
        childrenWithContributionList = childContributions.filter { it.wasDelivered == true && it.acceptance != null }
            .map { childContribution ->
                ChildrenWithContribution(
                    responsibleName = childContribution.child.family.responsibleName,
                    childName = childContribution.child.childName,
                    leaderName = childContribution.child.family.leadership.leaderName,
                    leaderColor = Color.valueOf(childContribution.child.family.leadership.leaderColor),
                )
            },

        childrenWithNoContributionList = children.filter { children ->
            childContributions.none { it.child.childId == children.childId }
        }.map { child ->
            ChildrenWithNoContribution(
                responsibleName = child.family.responsibleName,
                childName = child.childName,
                neighborhoodName = child.family.neighborhood.neighborhoodName,
                leaderName = child.family.leadership.leaderName
            )
        },
        childrenWithPendingContributionList = childContributions.filter {  (it.wasDelivered == false || it.toyDelivered == false) && it.acceptance == null }
            .map { childContribution ->
                ChildrenWithPendingContribution(
                    responsibleName = childContribution.child.family.responsibleName,
                    childName = childContribution.child.childName,
                    sponsorName = childContribution.sponsor.sponsorName,
                    sponsorPhone = childContribution.sponsor.sponsorPhone,
                    leaderName = childContribution.child.family.leadership.leaderName,
                    clothesDelivered = childContribution.wasDelivered,
                    toyDelivered = childContribution.toyDelivered

                )
            }
    )
}